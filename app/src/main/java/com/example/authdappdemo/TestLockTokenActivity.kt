package com.example.authdappdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.authdappdemo.wallet.WalletManager
import com.example.authdappdemo.wallet.WalletManagerLockTokenExtension.depositERC20FromContract
import com.example.authdappdemo.wallet.WalletManagerLockTokenExtension.withdrawERC20FromContract
import com.example.authdappdemo.wallet.WalletManagerLockTokenExtension.withdrawFromContract

class TestLockTokenActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent: Intent = Intent(context, TestLockTokenActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_lock_token)

        findViewById<Button>(R.id.btn_withdraw).setOnClickListener {
            WalletManager.withdrawFromContract(this)
        }

        findViewById<Button>(R.id.btn_deposit_erc20).setOnClickListener {
            WalletManager.depositERC20FromContract(this)
        }

        findViewById<Button>(R.id.btn_withdraw_erc20).setOnClickListener {
            WalletManager.withdrawERC20FromContract(this)
        }



    }

}