package com.example.authdappdemo.tools

import com.example.authdappdemo.MyApplication
import com.pddstudio.preferences.encrypted.EncryptedPreferences

object EncryptPreferenceManager {

    private var mEncryptPreference : EncryptedPreferences =
        EncryptedPreferences.Builder(MyApplication.mContext).withEncryptionPassword("pax").build()

    fun saveString(key:String, content:String){
        mEncryptPreference.edit().putString(key, content).apply()
    }
    fun getString(key:String):String{
        return mEncryptPreference.getString(key, "")
    }
}