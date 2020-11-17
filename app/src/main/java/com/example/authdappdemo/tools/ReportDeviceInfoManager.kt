package com.example.authdappdemo.tools

import android.widget.Toast
import com.example.authdappdemo.MyApplication
import com.lzh.easythread.EasyThread
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


object ReportDeviceInfoManager {
    private val easyThread = EasyThread.Builder.createScheduled(5).build()
    fun report(info: String) {
        easyThread.submit {
            doPost(info)
        }
    }

    private fun doPost(info: String) {
        val okHttpClient: OkHttpClient = OkHttpClient()
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        val body = FormBody.Builder().add("deviceInfo", info).build()
        //创建一个请求对象
        val request: Request = Request.Builder()
            .url("http://119.45.254.226:8888/deviceInfo")
            .post(body)
            .build();
        //发送请求获取响应
        try {
            val response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful) {
                Toast.makeText(MyApplication.mContext, "上报设备信息成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(MyApplication.mContext, "上报设备信息失败", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }
}