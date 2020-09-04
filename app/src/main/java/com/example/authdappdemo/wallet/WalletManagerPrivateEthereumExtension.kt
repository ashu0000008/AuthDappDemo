package com.example.authdappdemo.wallet

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.example.authdappdemo.model.LicenseSimple
import com.example.authdappdemo.tools.JsonTool
import me.leefeng.promptlibrary.PromptDialog
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger

object WalletManagerPrivateEthereumExtension {

    //私链钱包
    private var mWalletPrv: Credentials? = null
    var mContractId: String = "null"
    var mExpireAt: Long = System.currentTimeMillis()
    var mServiceUrl: String = "null"
    var mContractAddress: String = "null"
    var web3jPrv: Admin? = null

    fun importLicense(context: Context?, license: String) {
        //Todo 解析license格式，用私钥生成钱包，赋值合同Id
        val licenseSample = "{\"contractId\":\"product_customer_00\",\"prvKey\":\"b0ace7e3adcf4822436c860240b67880edc5e26ecec965fad7b8b73b126a158a\"}"
        var licenseSimple = JsonTool.jsonToObject(license, LicenseSimple::class.java)
        if (null == licenseSimple){
            Toast.makeText(context, "license格式错误", Toast.LENGTH_LONG).show()
            return
        }

        mWalletPrv = Credentials.create(licenseSimple?.privateKey)
        mContractId = licenseSimple?.licenseId.toString()
        mServiceUrl = licenseSimple?.serviceUrl.toString()
        mContractAddress = licenseSimple?.contractAddress.toString()
        web3jPrv = Admin.build(HttpService(mServiceUrl))
    }

    fun showBalancePrv(context: Context?) {
        if (null == mWalletPrv) {
            Toast.makeText(context, "无钱包", Toast.LENGTH_LONG).show()
            return
        }

        val balance = web3jPrv?.ethGetBalance(
            mWalletPrv?.address.toString(),
            DefaultBlockParameterName.LATEST
        )?.sendAsync()?.get()
        val bbbTmp = BigDecimal(balance?.balance.toString())
        val balanceEth = bbbTmp.divide(WalletConst.mOneEthDecimal, 8, BigDecimal.ROUND_FLOOR)
        Toast.makeText(context, balanceEth.toString(), Toast.LENGTH_LONG).show()
    }

    fun reqAuth(context: Activity?, deviceId: String) {

        val promptDialog = PromptDialog(context)
        promptDialog.showLoading("")

        WalletManager.easyThread.execute(Runnable {
            val canAuth = WalletManager.canAuth(context, mContractId)
            if (!canAuth) {
                context?.runOnUiThread {
                    promptDialog.showSuccess("")
                }
                return@Runnable
            }

            val nonce = web3jPrv?.ethGetTransactionCount(
                mWalletPrv?.address.toString(), DefaultBlockParameterName.LATEST
            )?.sendAsync()?.get()?.transactionCount

            val function = FunctionEncoder.makeFunction(
                "reqAuth", arrayListOf("string", "string"),
                arrayListOf(mContractId, deviceId) as List<Any>?, emptyList()
            )
            val encodedFunction = FunctionEncoder.encode(function)
            val rawTransaction = RawTransaction.createTransaction(
                nonce,
                WalletConfigure.mGasPrice,
                WalletConfigure.mGasLimit,
                mContractAddress,
                BigInteger.ZERO,
                encodedFunction
            )
            val signedString = TransactionEncoder.signMessage(rawTransaction, mWalletPrv)
            val transactionResponse =
                web3jPrv?.ethSendRawTransaction(Numeric.toHexString(signedString))
                    ?.sendAsync()?.get()
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

    fun checkAuth(context: Activity?, deviceId: String) {

        val promptDialog = PromptDialog(context)
        promptDialog.showLoading("")

        WalletManager.easyThread.execute(Runnable {
            val function = FunctionEncoder.makeFunction(
                "isAuthed", arrayListOf("string", "string"),
                arrayListOf(mContractId, deviceId) as List<Any>?, arrayListOf("bool", "string")
            )
            val encodedFunction = FunctionEncoder.encode(function)

            val transaction = Transaction.createEthCallTransaction(
                mWalletPrv?.address.toString(), mContractAddress, encodedFunction
            )
            val response =
                web3jPrv?.ethCall(transaction, DefaultBlockParameterName.LATEST)
                    ?.sendAsync()?.get()
            val returnValue =
                FunctionReturnDecoder.decode(response?.value, function.outputParameters)
            val result = returnValue[0].value as Boolean
            val reason = returnValue[1].value as String

            context?.runOnUiThread {
                promptDialog.showSuccess("")
                if (result) {
                    Toast.makeText(context, "已经得到授权", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "未授权:${reason}", Toast.LENGTH_LONG).show()
                }
            }

        })

    }
}