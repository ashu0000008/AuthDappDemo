package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EntranceActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent: Intent = Intent(context, EntranceActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        findViewById<TextView>(R.id.tv_test_prv_auth).setOnClickListener {
            DemoPrivateEtherActivity.start(this)
        }
        findViewById<TextView>(R.id.tv_test_android_id).setOnClickListener {
            TestAndroidIdActivity.start(this)
        }
    }
}