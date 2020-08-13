package com.example.authdappdemo.wallet

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService

import org.web3j.protocol.admin.Admin
import org.web3j.utils.Numeric
import java.math.BigInteger


object WalletManager {

    private var mWallet: Credentials? = null
    private var web3j:Admin? = Admin.build(HttpService(WalletConfigure.mEthNode))

    fun initWallet(){
        mWallet = WalletUtils.loadBip39Credentials("Test123", WalletConfigure.mMnemonic)
    }

    fun checkAuth():Boolean{
        val function = FunctionEncoder.makeFunction("isAuthed",
            arrayListOf("uint256"), arrayListOf(1234) as List<Any>?, arrayListOf("bool"))
        val encodedFunction = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(
            mWallet?.address.toString(), WalletConfigure.mContractAddress, encodedFunction)
        val response = web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val returnValue = FunctionReturnDecoder.decode(response?.value, function.outputParameters)
        return returnValue[0] as Boolean
    }

    fun requestAuth(context:Context?){

        //获取该账户交易个数
        val nonce = web3j?.ethGetTransactionCount(
            mWallet?.address.toString(), DefaultBlockParameterName.LATEST)?.sendAsync()?.get()?.transactionCount

        val function = FunctionEncoder.makeFunction("auth",
            arrayListOf("uint8", "uint256"), arrayListOf(0, 1234) as List<Any>?, emptyList())
        val encodedFunction = FunctionEncoder.encode(function)
//        val transaction = Transaction.createFunctionCallTransaction(mWallet?.address.toString(),
//            nonce, WalletConfigure.mGasPrice, WalletConfigure.mGasLimit,
//            WalletConfigure.mContractAddress, encodedFunction)

        val rawTransaction = RawTransaction.createContractTransaction(nonce, WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit, BigInteger.ZERO, encodedFunction)

        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse = web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)){
            if (null != context){
                Toast.makeText(context, hash, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context, "交易失败", Toast.LENGTH_LONG).show()
        }
    }
}