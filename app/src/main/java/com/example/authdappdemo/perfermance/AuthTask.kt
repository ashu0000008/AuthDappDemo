package com.example.authdappdemo.perfermance

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.authdappdemo.wallet.WalletConfigure
import org.web3j.abi.FunctionEncoder
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Numeric
import java.io.IOException
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicLong


class AuthTask(private val context: Activity?, val mDeviceId: String, private val web3jPrv: Admin?) {

    companion object {
        var mCounter = AtomicLong(0)
        var mTimeStart = System.currentTimeMillis()
    }

    private var mWalletPrv: Credentials? = null

    fun initIndeed() {
        mWalletPrv = Credentials.create(Configure2.mPrvKey)
//        mWalletPrv = AccountManager.getWallet()
    }


    fun doAuthOnce(): Boolean {
        val nonce = CurrentNonceTool.getCurrentNonce2(web3jPrv, mWalletPrv)

        val function = FunctionEncoder.makeFunction(
            "reqAuth4Performance", arrayListOf("string", "string"),
            arrayListOf(Configure2.mContractId, mDeviceId) as List<Any>?, arrayListOf("string")
        )
        val encodedFunction = FunctionEncoder.encode(function)

//        val gasLimit = getTransactionGasLimit(web3jPrv, )

        val rawTransaction = RawTransaction.createTransaction(
            nonce,
            Configure2.mGasPrice,
            Configure2.mGasLimit,
            Configure2.mContractAddress2,
            BigInteger.ZERO,
            encodedFunction
        )
        val signedString = TransactionEncoder.signMessage(rawTransaction, mWalletPrv)
        val transactionResponse = web3jPrv?.ethSendRawTransaction(Numeric.toHexString(signedString))
            ?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash
        if (TextUtils.isEmpty(hash)) {
            return false
        }

        //监听是否成功
        while (true) {
            Thread.sleep(1000)
            val receipt = web3jPrv?.ethGetTransactionReceipt(hash)?.sendAsync()?.get()

            //解析收据
            if (!receipt?.transactionReceipt?.isPresent!!) {
                continue
            }

            val countNow :Long
            synchronized(AuthTask::javaClass){
                countNow = mCounter.addAndGet(1)
            }

//            var timeSpend = (System.currentTimeMillis() - mTimeStart)/1000
//            Log.e("AuthTask", "完成认证数量：$mCounter------------------spend $timeSpend s")
//            EventBus.getDefault().post(AuthSuccessEvent(countNow, mDeviceId))
//            context?.runOnUiThread {
//                Toast.makeText(context, "完成认证数量：$mCounter", Toast.LENGTH_SHORT).show()
//            }

            if (Configure2.mAuthAmount <= countNow) {
                val tip =
                    "do ${Configure2.mAuthAmount} auth use :${(System.currentTimeMillis() - mTimeStart) / 1000}s"
                Log.e("AuthTask", tip)
                context?.runOnUiThread {
                    Toast.makeText(context, tip, Toast.LENGTH_LONG).show()
                }
            }

            return true
        }
    }


    private fun getTransactionGasLimit(
        web3j: Web3j,
        transaction: Transaction?
    ): BigInteger? {
        return try {
            val ethEstimateGas = web3j.ethEstimateGas(transaction).send()
            if (ethEstimateGas.hasError()) {
                throw RuntimeException(ethEstimateGas.error.message)
            }
            ethEstimateGas.amountUsed
        } catch (e: IOException) {
            throw RuntimeException("net error")
        }
    }
}