package com.example.authdappdemo

import android.app.Application
import android.content.Context
import com.example.authdappdemo.wallet.WalletManager

class MyApplication: Application() {
    companion object{
        var mContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        WalletManager.initWallet()
    }
}