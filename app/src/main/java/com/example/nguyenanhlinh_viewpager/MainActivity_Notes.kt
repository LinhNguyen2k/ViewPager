package com.example.nguyenanhlinh_viewpager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nguyenanhlinh_viewpager.model.Notes
import kotlinx.android.synthetic.main.activity_main_detail.*
import kotlinx.android.synthetic.main.activity_main_notes.*
import kotlinx.android.synthetic.main.activity_main_notes.img_back
import kotlinx.android.synthetic.main.activity_main_notes.tv_monthYearTV
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
        var localDate = LocalDate.now()
        val share = this.getSharedPreferences("date", Context.MODE_PRIVATE)
        val editor = share.edit()
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

        var checkDates = checkDate("$day/$month/$year")
        edt_notes.setText(checkDates.notes)
        edt_note = edt_notes.text.toString()

        tv_done.setOnClickListener {
//            if (edt_notes.text.toString() != null) {
//                if (checkDates.check) {
//                    val db = SQLite_Notes(this)
//                    val value: Boolean =
//                        db.updateData(edt_notes.text.toString(), checkDates.localdate)
//                    val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {

            if (edt_notes.text.toString() != null && checkDates.notes == "") {
                val db = SQLite_Notes(this)
                val value: Long = db.addNote(edt_notes.text.toString(), "$day/$month/$year")

                val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            } else if (edt_notes.text.toString() != null && checkDates.notes != "") {
                val db = SQLite_Notes(this)
                val value: Boolean =
                    db.updateData(edt_notes.text.toString(), checkDates.localdate)
                val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
                startActivity(intent)
                finish()
            } else {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }
//                    else{
//                        var localDate = LocalDate.now()
//                        val db = SQLite_Notes(this)
//                        val value: Long = db.addNote(edt_notes.text.toString(), "${localDate.dayOfMonth}/${localDate.month}/${localDate.year}")
//                        Log.d(TAG, "${day + month + year}")
//                        val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
//                        startActivity(intent)
//                        finish()
//                        return@setOnClickListener
//                    }

//                }
//            } else {
//
//            }
        }

        img_back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun checkDate(localDate: String): Notes {
        val sqliteNotes = SQLite_Notes(this)
        var list = sqliteNotes.getAllListNotes()
        if (list != null) {
            for (item in list) {
                if (localDate == item.localdate) {
                    return item
                }

            }
        }
        return Notes("", localDate, true)
    }
}