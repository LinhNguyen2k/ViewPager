package com.example.nguyenanhlinh_viewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_detail.*

class MainActivity_Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_detail)
        supportActionBar?.hide()
        val intent = intent

        var content = intent.getStringExtra("content").toString()
        var localDate = intent.getStringExtra("localdate").toString()
        edt_detail.setText(content)

        tv_done_detail.setOnClickListener {
            if (edt_detail.text.toString() == content){
                val intent = Intent(applicationContext,MainActivity_List_Notes::class.java)
                startActivity(intent)
            }else if (edt_detail.text.toString() != content){
                val db = SQLite_Notes(this)
                val value: Boolean =
                    db.updateData(edt_detail.text.toString(), localDate)
                val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
                startActivity(intent)
                finish()
            }
        }
        img_back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity_List_Notes::class.java)
            startActivity(intent)
            finish()
        }
    }
}