package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.tools.EncryptPreferenceManager

class TestSecSPActivity : AppCompatActivity(){

    companion object {
        fun start(context: Context) {
            val intent: Intent = Intent(context, TestSecSPActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sec_sp)
        initViews()
    }

    private fun initViews(){
        findViewById<Button>(R.id.btn_set).setOnClickListener{
            doSet()
        }
        findViewById<Button>(R.id.btn_get).setOnClickListener{
            doGet()
        }
    }

    private fun doSet(){
        EncryptPreferenceManager.saveString("TEST", "Hello ashu!")
    }

    private fun doGet(){
        val value = EncryptPreferenceManager.getString("TEST")
        Toast.makeText(this, value, Toast.LENGTH_LONG).show()
    }

}