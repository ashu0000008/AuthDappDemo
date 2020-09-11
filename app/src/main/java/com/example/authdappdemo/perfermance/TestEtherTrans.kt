package com.example.authdappdemo.perfermance

import android.app.Activity
import com.lzh.easythread.EasyThread
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.http.HttpService

object TestEtherTrans {
//    private val easyThread = EasyThread.Builder.createScheduled(200).build()
//    private var web3jPrv: Admin? = Admin.build(HttpService(Configure2.mEthNodePrivate))
//
//    fun startTest(context: Activity?) {
//
//        Thread {
//            for (i in 0..Configure2.mAuthAmount) {
//                val deviceId = i.toString()
//                val task = AuthTask(context, deviceId, web3jPrv)
//
//                easyThread.submit {
//                    task.initIndeed()
//                    while (true) {
//                        if (task.doAuthOnce()) {
//                            break
//                        }
//                    }
//
//                }
//            }
//        }.run()
//
//
//    }

}