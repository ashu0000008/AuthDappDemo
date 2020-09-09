package com.example.authdappdemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.perfermance.TestEtherTrans

class PerformanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            TestEtherTrans.startTest(this)
        }
    }
}