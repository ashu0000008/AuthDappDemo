package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.model.DeviceInfo
import com.example.authdappdemo.tools.ReportDeviceInfoManager

class TestAndroidIdActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent: Intent = Intent(context, TestAndroidIdActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_android_id)

        val androidId = getAndroidId()
        findViewById<TextView>(R.id.tv_android_id).text = androidId

        val deviceInfo = getDeviceInfo()
        ReportDeviceInfoManager.report(deviceInfo)
        findViewById<TextView>(R.id.tv_device_info).text = deviceInfo

        findViewById<TextView>(R.id.tv_device_info).setOnClickListener {
            if (it is TextView) {
                it.text = getDeviceInfo()
            }
        }
        findViewById<TextView>(R.id.tv_report).setOnClickListener {
            val info = getDeviceInfo()
            ReportDeviceInfoManager.report(info)
        }
    }

    private fun getAndroidId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun getDeviceInfo(): String {
        val brand = Build.BRAND
        val model = Build.MODEL
        val androidVersion = Build.VERSION.RELEASE
        val info = DeviceInfo(brand, model, androidVersion, getAndroidId())

        return TestJson.objectToJson(info) ?: ""
    }
}