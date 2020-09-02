package com.example.authdappdemo.wallet

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.lzh.easythread.EasyThread
import me.leefeng.promptlibrary.PromptDialog
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.crypto.Bip44WalletUtils
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

object WalletManager {

    var mWallet: Credentials? = null
    var web3j: Admin? = Admin.build(HttpService(WalletConfigure.mEthNodePrivate))
    val easyThread = EasyThread.Builder.createScheduled(5).build()

    fun initWallet() {
        mWallet = Bip44WalletUtils.loadBip44Credentials("Test123", WalletConfigure.mMnemonic, true)
        mWallet = Credentials.create(WalletConfigure.mPrvKey)
    }

    fun checkAuth(): Boolean {
        val function = FunctionEncoder.makeFunction(
            "isAuthed",
            arrayListOf("uint256"),
            arrayListOf(WalletConfigure.mDeviceId) as List<Any>?,
            arrayListOf(
                "bool"
            )
        )
        val encodedFunction = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(
            mWallet?.address.toString(), WalletConfigure.mContractAddress, encodedFunction
        )
        val response =
            web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val returnValue = FunctionReturnDecoder.decode(response?.value, function.outputParameters)
        val result = returnValue[0].value
        return result as Boolean
    }

    fun requestAuth(context: Context?) {
        //获取该账户交易个数
        val nonce = web3j?.ethGetTransactionCount(
            mWallet?.address.toString(), DefaultBlockParameterName.LATEST
        )?.sendAsync()?.get()?.transactionCount

        val function = FunctionEncoder.makeFunction(
            "auth",
            arrayListOf("uint8", "uint256"),
            arrayListOf(WalletConfigure.mContractIndex, WalletConfigure.mDeviceId) as List<Any>?,
            emptyList()
        )
        val encodedFunction = FunctionEncoder.encode(function)
        val rawTransaction = RawTransaction.createTransaction(
            nonce,
            WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit,
            WalletConfigure.mContractAddress,
            BigInteger.ZERO,
            encodedFunction
        )
        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse =
            web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)) {
            if (null != context) {
                Toast.makeText(context, "交易已发送到链上:$hash", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                context,
                "交易失败:" + transactionResponse?.error?.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun showBalance(context: Context?) {
        val balance = web3j?.ethGetBalance(
            mWallet?.address.toString(),
            DefaultBlockParameterName.LATEST
        )?.sendAsync()?.get()
        val bbbTmp = BigDecimal(balance?.balance.toString())
        val balanceEth = bbbTmp.divide(WalletConst.mOneEthDecimal, 8, BigDecimal.ROUND_FLOOR)
        Toast.makeText(context, balanceEth.toString(), Toast.LENGTH_LONG).show()
    }

    fun showAddress(context: Context?) {
        val address = mWallet?.address.toString()
        Toast.makeText(context, address, Toast.LENGTH_LONG).show()
    }

    fun doTransfer(context: Context?) {
        val targetAddress = "0x281BA3d06B0347B1f8b1391C243D16cAE1001c34"
        val value = WalletConst.mOneEth.divide(BigInteger("10"))

        //获取该账户交易个数
        val nonce = web3j?.ethGetTransactionCount(
            mWallet?.address.toString(), DefaultBlockParameterName.LATEST
        )?.sendAsync()?.get()?.transactionCount

        val rawTransaction = RawTransaction.createEtherTransaction(
            nonce, WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit, targetAddress, value
        )

        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse =
            web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)) {
            if (null != context) {
                Toast.makeText(context, "转账成功:$hash", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                context,
                "转账失败:" + transactionResponse?.error?.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    //新版demo
    fun addContract(context: Activity?, id: String, amount: Long, expire: Long) {
        if (0L == amount || 0L == expire) {
            return
        }

        val promptDialog =  PromptDialog(context)
        promptDialog.showLoading("")

        easyThread.execute(Runnable {
            val nonce = web3j?.ethGetTransactionCount(
                mWallet?.address.toString(), DefaultBlockParameterName.LATEST
            )?.sendAsync()?.get()?.transactionCount

            val function = FunctionEncoder.makeFunction(
                "addContract", arrayListOf(
                    "string",
                    "uint256",
                    "uint256"
                ),
                arrayListOf(id, amount, expire) as List<Any>?, arrayListOf("bool")
            )
            val encodedFunction = FunctionEncoder.encode(function)
            val rawTransaction = RawTransaction.createTransaction(
                nonce,
                WalletConfigure.mGasPrice,
                WalletConfigure.mGasLimit,
                WalletConfigure.mContractAddressNew,
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
                    Toast.makeText(context, "交易已发送到链上:$hash", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        context,
                        "交易失败:" + transactionResponse?.error?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

    }

    private fun canAuth(context: Activity?, contractId: String): Boolean {
        val function = FunctionEncoder.makeFunction(
            "canAuth", arrayListOf("string"),
            arrayListOf(contractId) as List<Any>?, arrayListOf("bool", "string")
        )
        val encodedFunction = FunctionEncoder.encode(function)

        val transaction = Transaction.createEthCallTransaction(
            mWallet?.address.toString(), WalletConfigure.mContractAddressNew, encodedFunction
        )
        val response =
            web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val returnValue = FunctionReturnDecoder.decode(response?.value, function.outputParameters)
        val result = returnValue[0].value as Boolean
        val reason = returnValue[1].value as String

        context?.runOnUiThread {
            if (result) {
            } else {
                Toast.makeText(context, "授权失败:$reason", Toast.LENGTH_LONG).show()
            }
        }

        return result
    }

    fun reqAuth(context: Activity?, contractId: String, deviceId: String) {

        val promptDialog =  PromptDialog(context)
        promptDialog.showLoading("")

        easyThread.execute(Runnable {
            val canAuth = canAuth(context, contractId)
            if (!canAuth) {
                context?.runOnUiThread {
                    promptDialog.showSuccess("")
                }
                return@Runnable
            }

            val nonce = web3j?.ethGetTransactionCount(
                mWallet?.address.toString(), DefaultBlockParameterName.LATEST
            )?.sendAsync()?.get()?.transactionCount

            val function = FunctionEncoder.makeFunction(
                "reqAuth", arrayListOf("string", "string"),
                arrayListOf(contractId, deviceId) as List<Any>?, emptyList()
            )
            val encodedFunction = FunctionEncoder.encode(function)
            val rawTransaction = RawTransaction.createTransaction(
                nonce,
                WalletConfigure.mGasPrice,
                WalletConfigure.mGasLimit,
                WalletConfigure.mContractAddressNew,
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


//        Thread(Runnable {
//            while (true){
//                Thread.sleep(2000)
//                val transactionReceipt = web3j?.ethGetTransactionReceipt(hash)?.sendAsync()?.get()?.transactionReceipt
//                if (transactionReceipt!!.isPresent){
//                    break
//                }
//            }
//        }).run();
    }

    fun checkAuth(context: Activity?, contractId: String, deviceId: String) {

        val promptDialog =  PromptDialog(context)
        promptDialog.showLoading("")

        easyThread.execute(Runnable {
            val function = FunctionEncoder.makeFunction(
                "isAuthed", arrayListOf("string", "string"),
                arrayListOf(contractId, deviceId) as List<Any>?, arrayListOf("bool", "string")
            )
            val encodedFunction = FunctionEncoder.encode(function)

            val transaction = Transaction.createEthCallTransaction(
                mWallet?.address.toString(), WalletConfigure.mContractAddressNew, encodedFunction
            )
            val response =
                web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
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