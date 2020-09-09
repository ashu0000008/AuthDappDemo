package com.example.authdappdemo.perfermance

import android.app.Activity
import com.lzh.easythread.EasyThread
import java.util.UUID.randomUUID

object TestEtherTrans {
    private val easyThread = EasyThread.Builder.createScheduled(100).build()

    fun startTest(context: Activity?) {
        for (i in 0..Configure2.mAuthAmount) {
            val deviceId = randomUUID().toString()
            val task = AuthTask(context, deviceId)
            easyThread.submit {
                task.initIndeed()
                while (true){
                    if (task.doAuthOnce()){
                        break
                    }
                }

            }
        }
    }

}