package com.example.authdappdemo

import android.app.Application
import com.example.authdappdemo.wallet.WalletManager

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        WalletManager.initWallet()
    }
}