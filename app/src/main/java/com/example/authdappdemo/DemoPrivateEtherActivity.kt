package com.example.authdappdemo

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.wallet.WalletConfigure
import com.example.authdappdemo.wallet.WalletManagerPrivateEthereumExtension

class DemoPrivateEtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_private_etheremu)
        initView()
        initCheckAuth()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_old_version).setOnClickListener {
            DemoMainActivity.start(this)
        }
        findViewById<Button>(R.id.btn_import_license).setOnClickListener {
            importLicense()
        }
        findViewById<Button>(R.id.btn_check_balance).setOnClickListener {
            WalletManagerPrivateEthereumExtension.showBalancePrv(this)
        }
    }

    private fun importLicense() {
        var etLicense = findViewById<EditText>(R.id.et_license)
        val license = etLicense.text.toString()
//        if (TextUtils.isEmpty(license)) {
//            Toast.makeText(this, "license is null", Toast.LENGTH_LONG).show()
//            return
//        }

        WalletManagerPrivateEthereumExtension.importLicense(this, license)

        val etContractId = findViewById<TextView>(R.id.et_contract_id)
        etContractId.text = WalletManagerPrivateEthereumExtension.mContractId
    }

    private fun initCheckAuth() {
        val etContractId = findViewById<TextView>(R.id.et_contract_id)
        val etDeviceId = findViewById<TextView>(R.id.et_device_id)
        etDeviceId.text = WalletConfigure.mDeviceId

        findViewById<Button>(R.id.btn_auth_req).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)
            ) {
                return@setOnClickListener
            }

            try {
                WalletManagerPrivateEthereumExtension.reqAuth(this, deviceId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        findViewById<Button>(R.id.btn_auth_check).setOnClickListener {
            val contractId = etContractId.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (TextUtils.isEmpty(contractId)
                || TextUtils.isEmpty(deviceId)
            ) {
                return@setOnClickListener
            }

            try {
                WalletManagerPrivateEthereumExtension.checkAuth(this, deviceId)
            } catch (e: Exception) {
            }
        }
    }
}