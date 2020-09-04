package com.example.authdappdemo.tools

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException

class JsonTool {
    companion object {
        fun <T> jsonToObject(jsonData: String?, clazz: Class<T>?): T? {
            var t: T? = null
            if (TextUtils.isEmpty(jsonData)) {
                return null
            }
            try {
                t = JSON.parseObject(jsonData, clazz)
            } catch (e: java.lang.Exception) {
                Log.e("jsonToObject", e.toString())
            }
            return t
        }

        fun objectToJson(`object`: Any?): String? {
            if (`object` == null) {
                return ""
            }
            try {
                return JSON.toJSONString(`object`)
            } catch (e: JSONException) {
            } catch (e: Exception) {
            }
            return ""
        }
    }
}