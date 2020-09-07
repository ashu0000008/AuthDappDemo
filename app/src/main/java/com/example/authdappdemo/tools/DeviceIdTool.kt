package com.example.authdappdemo.tools

import android.app.Service
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import java.util.*


class DeviceIdTool {
    companion object {

        //android Q以上版本，禁止非system应用获取IMEI
        fun getImei(context: Context): String {
            val service: TelephonyManager =
                context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            return service.imei
        }

        fun getUniqueId(): String {
            val uniquePseudoID =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
            val serial = Build.getRadioVersion()
            val uuid: String =
                UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
            Log.e("uuid", uuid)
            return uuid
        }
    }
}