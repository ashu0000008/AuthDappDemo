package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.wallet.WalletConfigure
import com.example.authdappdemo.wallet.WalletManager
import java.lang.Exception

class DemoMainActivity  : AppCompatActivity(){

    companion object{
        fun start(context: Context){
            val intent: Intent = Intent(context, DemoMainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        initView()
    }

    private fun initView(){
        val etContractId = findViewById<EditText>(R.id.et_contract_id)
        val etDeviceId = findViewById<EditText>(R.id.et_device_id)
        etContractId.setText(WalletConfigure.mContractId)
        etDeviceId.setText(WalletConfigure.mDeviceId.toString())

        findViewById<Button>(R.id.btn_add_contract).setOnClickListener {
            ContractAddActivity.start(this)
        }
        findViewById<Button>(R.id.btn_old_version).setOnClickListener {
            MainActivity.start(this)
        }
        findViewById<Button>(R.id.btn_contract_transfer_token).setOnClickListener {
            TestLockTokenActivity.start(this)
        }

        findViewById<Button>(R.id.btn_auth_req).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)){
                return@setOnClickListener
            }

            try{
                WalletManager.reqAuth(this, contractId, deviceId)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        findViewById<Button>(R.id.btn_auth_check).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)){
                return@setOnClickListener
            }

            try{
                WalletManager.checkAuth(this, contractId, deviceId)
            }catch (e:Exception){
            }
        }
    }
}