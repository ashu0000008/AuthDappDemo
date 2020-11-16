package com.example.authdappdemo

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.example.authdappdemo.model.License
import com.example.authdappdemo.model.ProductInfo

object TestJson {

//    fun doTest(){
//        val license = LicenseSimple("b0ace7e3adcf4822436c860240b67880edc5e26ecec965fad7b8b73b126a158a",
//            "product_customer_00")
//        val jsonString = objectToJson(license)
//        Log.e("license", jsonString.toString())
//    }

    fun doTest() {
        val license = License("xxxx", "ooooo", ProductInfo("pp", "oo"))
        val jsonString = objectToJson(license)

        val licenseResult = jsonToObject(jsonString, License::class.java)
        Log.e("xxx", licenseResult.toString())
    }

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