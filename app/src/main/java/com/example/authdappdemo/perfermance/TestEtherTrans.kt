package com.example.authdappdemo.perfermance

import android.app.Activity
import com.lzh.easythread.EasyThread

object TestEtherTrans {
    private val easyThread = EasyThread.Builder.createFixed(200).build()

    fun startTest(context: Activity?) {

        Thread {
            for (i in 0..Configure2.mAuthAmount) {
                val deviceId = i.toString()
                val task = AuthTask(context, deviceId)

                easyThread.submit {
                    task.initIndeed()
                    while (true) {
                        if (task.doAuthOnce()) {
                            break
                        }
                    }

                }
            }
        }.run()


    }

}