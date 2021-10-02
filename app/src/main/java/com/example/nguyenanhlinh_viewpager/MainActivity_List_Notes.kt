package com.example.nguyenanhlinh_viewpager

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nguyenanhlinh_viewpager.adapter.ListNoteAdapter
import com.example.nguyenanhlinh_viewpager.model.Notes
import com.example.nguyenanhlinh_viewpager.model.WriteReadFile
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_main_list_notes.*
import kotlinx.android.synthetic.main.activity_main_list_notes.img_back
import kotlinx.android.synthetic.main.activity_main_notes.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class MainActivity_List_Notes : AppCompatActivity() {
    lateinit var listNotes: ArrayList<Notes>
    lateinit var listNoteAdapter: ListNoteAdapter
    private lateinit var storagePermission: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list_notes)

        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        supportActionBar?.hide()
        var writeFile = WriteReadFile(this)
        val sqliteNotes = SQLite_Notes(this)
        listNotes = sqliteNotes.getAllListNotes()!!
        sortDayOfMonth()
        listNoteAdapter = ListNoteAdapter(listNotes, applicationContext)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        rc_list_note.setHasFixedSize(true)
        rc_list_note.layoutManager = linearLayoutManager
        rc_list_note.adapter = listNoteAdapter
        listNoteAdapter.notifyDataSetChanged()

        img_back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        edt_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
        tv_backup.setOnClickListener {
            writeFile.writeFile(sqliteNotes.getAllListNotes()!!)
            Toast.makeText(this, "Backup thành công", Toast.LENGTH_SHORT).show()
        }
        tv_restore.setOnClickListener {
            val delete: Int = sqliteNotes.deleteAllNotes()
            var list = writeFile.readFile()
            sqliteNotes.insertList(list)
            list.forEach {
                Log.d("localdate :",it.localdate)
                Log.d("notes :",it.notes)
            }
            listNotes = sqliteNotes.getAllListNotes()!!
            listNoteAdapter = ListNoteAdapter(listNotes, applicationContext)
            rc_list_note.adapter = listNoteAdapter
            listNoteAdapter.notifyDataSetChanged()

        }

    }

    private fun filter(text: String) {
        val filter: ArrayList<Notes> = ArrayList()
        for (item in listNotes) {
            if (item.notes.lowercase().contains(text.lowercase()))
                filter.add(item)
        }
        listNoteAdapter.filterList(filter)
    }

    fun sortDayOfMonth() {
        listNotes.sortBy { it.localdate }
    }

}