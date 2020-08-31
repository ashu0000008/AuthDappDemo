package com.example.authdappdemo.wallet

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import me.leefeng.promptlibrary.PromptDialog
import org.web3j.abi.FunctionEncoder
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger

object WalletManagerLockTokenExtension {
    fun WalletManager.withdrawFromContract(context: Activity?){
        val promptDialog =  PromptDialog(context)
        promptDialog.showLoading("")

        easyThread.execute(Runnable {
            val nonce = web3j?.ethGetTransactionCount(
                mWallet?.address.toString(), DefaultBlockParameterName.LATEST
            )?.sendAsync()?.get()?.transactionCount

            val function = FunctionEncoder.makeFunction(
                "withdraw", arrayListOf("uint256"),
                arrayListOf(WalletConst.mWithdrawEth) as List<Any>?, emptyList()
            )
            val encodedFunction = FunctionEncoder.encode(function)
            val rawTransaction = RawTransaction.createTransaction(
                nonce,
                WalletConfigure.mGasPrice,
                WalletConfigure.mGasLimit,
                "0xd6De6eEEC1F3D775A463A5BF81897359cb2939F5",
                BigInteger.ZERO,
                encodedFunction
            )
            val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
            val transactionResponse =
                web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
            val hash = transactionResponse?.transactionHash

            context?.runOnUiThread {
                promptDialog.showSuccess("")
                if (!TextUtils.isEmpty(hash)) {
                    Toast.makeText(context, "交易已发送到链上:${hash}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        context,
                        "交易失败:${transactionResponse?.error?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }
}