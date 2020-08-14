package com.example.authdappdemo.wallet

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.crypto.*
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService

import org.web3j.protocol.admin.Admin
import org.web3j.utils.Numeric
import java.math.BigDecimal

object WalletManager {

    private var mWallet: Credentials? = null
    private var web3j:Admin? = Admin.build(HttpService(WalletConfigure.mEthNode))

    fun initWallet(){
//        mWallet = Bip44WalletUtils.loadBip44Credentials("Test123", WalletConfigure.mMnemonic, true)
        mWallet = Credentials.create(WalletConfigure.mPrvKey)
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

        val rawTransaction = RawTransaction.createTransaction(nonce, WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit, WalletConfigure.mContractAddress, encodedFunction)

        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse = web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)){
            if (null != context){
                Toast.makeText(context, hash, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context, "交易失败:"+transactionResponse?.error?.message, Toast.LENGTH_LONG).show()
        }
    }

    fun showBalance(context:Context?){
        val balance = web3j?.ethGetBalance(mWallet?.address.toString(), DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val bbbTmp = BigDecimal(balance?.balance.toString())
        val balanceEth = bbbTmp.divide(BigDecimal("1000000000000000000"), 8, BigDecimal.ROUND_FLOOR)
        Toast.makeText(context, balanceEth.toString(), Toast.LENGTH_LONG).show()
    }

    fun showAddress(context: Context?){
        val address = mWallet?.address.toString()
        Toast.makeText(context, address, Toast.LENGTH_LONG).show()
    }
}