package com.example.nguyenanhlinh_viewpager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_notes.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity_Notes : AppCompatActivity() {
    private val TAG = "MainActivity_Notes"
    private var edt_note = ""
    private var checked = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_notes)
        supportActionBar?.hide()
        val share = this.getSharedPreferences("date", Context.MODE_PRIVATE)
        val day = share.getInt("dayOfWeek", 0)
        val month = share.getInt("month", 0)
        val year = share.getInt("year", 0)
        tv_monthYearTV.text = "Ghi chú"
//        val shared = applicationContext.getSharedPreferences("push_detail", Context.MODE_PRIVATE)
//        edt_note = shared.getString("content", "").toString()
//        checked = shared.getInt("check", 0)
//
//        if (edt_note == "") {
//            edt_notes.setText("")
//        } else {
//            edt_notes.setText(edt_note)
//        }


        tv_done.setOnClickListener {
            if (edt_notes.text.toString() != null) {
                val db = SQLite_Notes(this)
                val value: Long = db.addNote(edt_notes.text.toString(), "$day/$month/$year")
                Log.d(TAG, "${day + month + year}")
                val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }
        }

        img_back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}