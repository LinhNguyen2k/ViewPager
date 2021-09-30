package com.example.nguyenanhlinh_viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.onbroading.*

class OnBroadingActivity : AppCompatActivity() {
    var check = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onbroading)
        supportActionBar?.hide()
        var shared = getSharedPreferences("put_pass", Context.MODE_PRIVATE)
        var editor = shared.edit()
        btn_login.setOnClickListener {

            if (shared.getString("pass", "") == "") {
                if (edt_passWord.text.toString().isEmpty()) {
                    Toast.makeText(applicationContext, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener

                }
                editor.putString("pass", edt_passWord.text.toString())
                editor.commit()
                Log.d("pass", "${shared.getString("pass", "")}")
            } else {
                if (shared.getString("pass", "") != edt_passWord.text.toString()){
                    Toast.makeText(applicationContext, "Mật khẩu sai", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}