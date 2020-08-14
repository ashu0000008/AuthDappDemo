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
import java.math.BigInteger

object WalletManager {

    private var mWallet: Credentials? = null
    private var web3j:Admin? = Admin.build(HttpService(WalletConfigure.mEthNode))

    fun initWallet(){
//        mWallet = Bip44WalletUtils.loadBip44Credentials("Test123", WalletConfigure.mMnemonic, true)
        mWallet = Credentials.create(WalletConfigure.mPrvKey)
    }

    fun checkAuth():Boolean{
        val function = FunctionEncoder.makeFunction("isAuthed",
            arrayListOf("uint256"), arrayListOf(WalletConfigure.mDeviceId) as List<Any>?, arrayListOf("bool"))
        val encodedFunction = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(
            mWallet?.address.toString(), WalletConfigure.mContractAddress, encodedFunction)
        val response = web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val returnValue = FunctionReturnDecoder.decode(response?.value, function.outputParameters)
        val result = returnValue[0].value
        return result as Boolean
    }

    fun requestAuth(context:Context?){

        //获取该账户交易个数
        val nonce = web3j?.ethGetTransactionCount(
            mWallet?.address.toString(), DefaultBlockParameterName.LATEST)?.sendAsync()?.get()?.transactionCount

        val function = FunctionEncoder.makeFunction("auth", arrayListOf("uint8", "uint256"),
            arrayListOf(WalletConfigure.mContractIndex, WalletConfigure.mDeviceId) as List<Any>?, emptyList())
        val encodedFunction = FunctionEncoder.encode(function)
//        val transaction = Transaction.createFunctionCallTransaction(mWallet?.address.toString(),
//            nonce, WalletConfigure.mGasPrice, WalletConfigure.mGasLimit,
//            WalletConfigure.mContractAddress, encodedFunction)

        //fee 0.1eth
//        val value = WalletConst.mOneEth.divide(BigInteger("10"))

        val rawTransaction = RawTransaction.createTransaction(nonce, WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit, WalletConfigure.mContractAddress, BigInteger.ZERO, encodedFunction)

        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse = web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)){
            if (null != context){
                Toast.makeText(context, "交易已发送到链上:$hash", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context, "交易失败:"+transactionResponse?.error?.message, Toast.LENGTH_LONG).show()
        }
    }

    fun showBalance(context:Context?){
        val balance = web3j?.ethGetBalance(mWallet?.address.toString(), DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        val bbbTmp = BigDecimal(balance?.balance.toString())
        val balanceEth = bbbTmp.divide(WalletConst.mOneEthDecimal, 8, BigDecimal.ROUND_FLOOR)
        Toast.makeText(context, balanceEth.toString(), Toast.LENGTH_LONG).show()
    }

    fun showAddress(context: Context?){
        val address = mWallet?.address.toString()
        Toast.makeText(context, address, Toast.LENGTH_LONG).show()
    }

    fun doTransfer(context: Context?){
        val targetAddress = "0x281BA3d06B0347B1f8b1391C243D16cAE1001c34"
        val value = WalletConst.mOneEth.divide(BigInteger("10"))

        //获取该账户交易个数
        val nonce = web3j?.ethGetTransactionCount(
            mWallet?.address.toString(), DefaultBlockParameterName.LATEST)?.sendAsync()?.get()?.transactionCount

        val rawTransaction = RawTransaction.createEtherTransaction(nonce, WalletConfigure.mGasPrice,
            WalletConfigure.mGasLimit, targetAddress, value)

        val signedString = TransactionEncoder.signMessage(rawTransaction, mWallet)
        val transactionResponse = web3j?.ethSendRawTransaction(Numeric.toHexString(signedString))?.sendAsync()?.get()
        val hash = transactionResponse?.transactionHash

        if (!TextUtils.isEmpty(hash)){
            if (null != context){
                Toast.makeText(context, "转账成功:$hash", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(context, "转账失败:"+transactionResponse?.error?.message, Toast.LENGTH_LONG).show()
        }
    }
}