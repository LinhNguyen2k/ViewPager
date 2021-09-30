package com.example.nguyenanhlinh_viewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nguyenanhlinh_viewpager.adapter.ListNoteAdapter
import com.example.nguyenanhlinh_viewpager.model.Notes
import kotlinx.android.synthetic.main.activity_main_list_notes.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity_List_Notes : AppCompatActivity() {
    lateinit var listNotes: ArrayList<Notes>
    lateinit var listNoteAdapter: ListNoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list_notes)
        supportActionBar?.hide()
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
            val intent = Intent(applicationContext,MainActivity::class.java)
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