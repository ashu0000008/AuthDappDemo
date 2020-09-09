package com.example.authdappdemo

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.model.AuthSuccessEvent
import com.example.authdappdemo.perfermance.TestEtherTrans
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PerformanceActivity : AppCompatActivity() {

    private var mTvStatus :TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            TestEtherTrans.startTest(this)
        }
        mTvStatus = findViewById<TextView>(R.id.tv_status)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AuthSuccessEvent) {
        mTvStatus?.text = "已完成:${event.count}\n\n" + "最近认证device:${event.deviceId}"
    }
}