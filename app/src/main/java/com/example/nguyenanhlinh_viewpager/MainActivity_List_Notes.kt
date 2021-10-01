package com.example.nguyenanhlinh_viewpager

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nguyenanhlinh_viewpager.adapter.ListNoteAdapter
import com.example.nguyenanhlinh_viewpager.model.Notes
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
    private val BACKUP = 1
    private val RESTORE = 2
    private lateinit var storagePermission: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list_notes)

        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
            if (checkStoragePermission()) {
                backUp()
            } else {
                requestStoragePermissionExport()
            }
        }
        tv_restore.setOnClickListener {
            if (checkStoragePermission()) {
                val delete : Int = sqliteNotes.deleteAllNotes()
                reStore()
                onResume()
                listNotes = sqliteNotes.getAllListNotes()!!
                listNoteAdapter = ListNoteAdapter(listNotes, applicationContext)
                rc_list_note.adapter = listNoteAdapter
                listNoteAdapter.notifyDataSetChanged()
            } else {
                requestStoragePermissionImport()
            }
        }

    }

    fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermissionImport() {
        ActivityCompat.requestPermissions(this, storagePermission, BACKUP)
    }

    private fun requestStoragePermissionExport() {
        ActivityCompat.requestPermissions(this, storagePermission, RESTORE)
    }

    fun backUp() {
        val folder = File(Environment.getExternalStorageDirectory()
            .toString() + "/" + "NguyenAnhLinh_ViewPager")

        var isFolderCreated = false

        if (!folder.exists())
            isFolderCreated = folder.mkdir()
        val csvFileName = "SQLite_Backup.csv"

        val fileNameAndPath = "$folder/$csvFileName"

        var dateList = ArrayList<Notes>()
        val sqliteNotes = SQLite_Notes(this)
        dateList.clear()
        dateList = sqliteNotes.getAllListNotes()!!

        try {
            val fw = FileWriter(fileNameAndPath)
            for (i in dateList.indices) {
                fw.append("" + dateList[i].notes)
                fw.append(",")
                fw.append("" + dateList[i].localdate)
                fw.append("\n")
            }
            fw.flush()
            fw.close()
            Toast.makeText(this, "Backup success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun reStore() {
        val filePathAddName =
            "${Environment.getExternalStorageDirectory()}/NguyenAnhLinh_ViewPager/SQLite_Backup.csv"

        val cvsFile = File(filePathAddName)

        if (cvsFile.exists()) {
            try {
                val csvReader = CSVReader(FileReader(cvsFile.absoluteFile))
                var nextLine: Array<String>
                while (csvReader.readNext().also { nextLine = it } != null) {
                    val content = nextLine[0]
                    val localedate = nextLine[1]

                    val db = SQLite_Notes(this)
                    val value: Long = db.addNote(content, localedate)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "import success", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            BACKUP -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    backUp()
                } else {
                    Toast.makeText(this, "Không được cho phép", Toast.LENGTH_SHORT).show()
                }
            }
            RESTORE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    backUp()
                } else {
                    Toast.makeText(this, "Không được cho phép", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}