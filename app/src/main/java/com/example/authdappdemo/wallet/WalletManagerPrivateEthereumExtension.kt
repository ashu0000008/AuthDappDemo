package com.example.authdappdemo.wallet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.authdappdemo.model.LicenseSimple
import com.example.authdappdemo.tools.JsonTool
import com.example.authdappdemo.wallet.WalletManager.easyThread
import me.leefeng.promptlibrary.PromptDialog
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
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
    var mServiceUrl: String = WalletConfigure.mEthNodePrivate
    var mContractAddress: String = WalletConfigure.mContractAddressNew
    var web3jPrv: Admin? = null

    fun importLicense(context: Activity?, license: String) {
//        val licenseSample = "{\"licenseId\":\"product_customer_00\",\"privateKey\":\"b0ace7e3adcf4822436c860240b67880edc5e26ecec965fad7b8b73b126a158a\"}"
        var licenseSimple = JsonTool.jsonToObject(license, LicenseSimple::class.java)
        if (null == licenseSimple){
            Toast.makeText(context, "license格式错误", Toast.LENGTH_LONG).show()
            return
        }

        mWalletPrv = Credentials.create(licenseSimple?.privateKey)
        if (!TextUtils.isEmpty(licenseSimple?.licenseId)){
            mContractId = licenseSimple?.licenseId.toString()
        }
        if (!TextUtils.isEmpty(licenseSimple?.serviceUrl)){
            mServiceUrl = licenseSimple?.serviceUrl.toString()
            if (mServiceUrl.startsWith("_")){
                mServiceUrl = mServiceUrl.substring(1)
            }
        }
        if (!TextUtils.isEmpty(licenseSimple?.contractAddress)){
            mContractAddress = licenseSimple?.contractAddress.toString()
        }

        web3jPrv = Admin.build(HttpService(mServiceUrl))

        //just test
//        mContractAddress = WalletConfigure.mContractAddressNew
//        mContractId = WalletConfigure.mContractId

//        startListening(context)
        ChainEventManager2.startListening(context, web3jPrv, mContractAddress);
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
                    Toast.makeText(context, "有权限", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "无权限:${reason}", Toast.LENGTH_LONG).show()
                }
            }

        })
    }


    @SuppressLint("CheckResult")
    private fun startListening(context: Activity?){
        val event = Event(
            "authedEventFinal",
            arrayListOf(
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Utf8String>() {}
            ) as List<TypeReference<*>>?
        )

        val filter = EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            WalletConfigure.mContractAddressNew
        )
        filter.addSingleTopic(EventEncoder.encode(event))

        easyThread.execute {
            Runnable {
                web3jPrv?.ethLogFlowable(filter)?.subscribe {

                    val args = FunctionReturnDecoder.decode(
                        it.data, event.parameters
                    )

                    context?.runOnUiThread {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    }

                }.runCatching {
                    Log.e("xx", "ooo")
                }
            }
        }

    }

    fun doEventResponse(context: Activity?, contractId: String, deviceId: String) {
        if (contractId == mContractId && deviceId == WalletConfigure.mDeviceId) {
            Toast.makeText(context, "申请授权成功", Toast.LENGTH_LONG).show()
        }
    }
}