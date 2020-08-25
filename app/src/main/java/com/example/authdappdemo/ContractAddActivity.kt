package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.wallet.WalletConfigure
import com.example.authdappdemo.wallet.WalletConst
import com.example.authdappdemo.wallet.WalletManager

class ContractAddActivity : AppCompatActivity(){

    companion object{
        fun start(context: Context){
            val intent:Intent = Intent(context, ContractAddActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_add)
        initView()
    }

    private fun initView(){
        val etContractId = findViewById<EditText>(R.id.et_contract_id)
        val etContractAmount = findViewById<EditText>(R.id.et_contract_amount)
        val etContractExpire = findViewById<EditText>(R.id.et_contract_expire)
        etContractId.setText(WalletConfigure.mContractId)
        etContractAmount.setText("2")
        etContractExpire.setText("100")

        findViewById<Button>(R.id.btn_add).setOnClickListener {
            doAddContractToBlockChain()
        }
    }

    private fun doAddContractToBlockChain(){
        val etContractId = findViewById<EditText>(R.id.et_contract_id)
        val etContractAmount = findViewById<EditText>(R.id.et_contract_amount)
        val etContractExpire = findViewById<EditText>(R.id.et_contract_expire)

        val contractId = etContractId.text.toString()
        val contractAmount = etContractAmount.text.toString()
        val contractExpire = etContractExpire.text.toString()

        if (TextUtils.isEmpty(contractId)
            || TextUtils.isEmpty(contractAmount)
            || TextUtils.isEmpty(contractExpire)){
            return
        }

        WalletManager.addContract(this, contractId, contractAmount.toLong(), contractExpire.toLong())
    }
}