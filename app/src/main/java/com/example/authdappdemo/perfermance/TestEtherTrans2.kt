package com.example.authdappdemo.perfermance

import android.app.Activity
import android.util.Log
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.http.HttpService
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

//使用原生线程池
object TestEtherTrans2 {
    private var mThreadPoolExecutor: ThreadPoolExecutor
    private var web3jPrv: Admin? = Admin.build(HttpService(Configure2.mEthNodePrivate))

    init {
        val workQueue: BlockingQueue<Runnable> = ArrayBlockingQueue(10000, true)
        mThreadPoolExecutor = ThreadPoolExecutor(
            100, 400, 1000,
            TimeUnit.SECONDS, workQueue
        )
    }

    fun startTest(context: Activity?) {
        AuthEventManager.startListening(web3jPrv)
        mThreadPoolExecutor.rejectedExecutionHandler =
            RejectedExecutionHandler { p0, _ ->
                val runnable = p0 as MyRunnable
                Log.e("rejectedExecution", runnable.task.mDeviceId)
            }

        Thread {
            for (i in 1..Configure2.mAuthAmount) {

                //任务投递休息
                if (i%100 == 0L){
                    Thread.sleep(5000)
                }

                val deviceId = "100000$i"
                val task = AuthTask(context, deviceId, web3jPrv)
                mThreadPoolExecutor.submit(MyRunnable(task))
            }
        }.run()
    }

    class MyRunnable (val task:AuthTask): Runnable {
        override fun run() {
            task.initIndeed()
            while (true) {
                if (task.doAuthOnce()) {
                    break
                }
            }
        }

    }
}