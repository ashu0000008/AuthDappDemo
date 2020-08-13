package com.example.authdappdemo.wallet

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService

import org.web3j.protocol.admin.Admin


object WalletManager {

    private var mWallet: Credentials? = null
    private var web3j:Admin? = Admin.build(HttpService(WalletConfigure.mEthNode))

    fun initWallet(){
        mWallet = WalletUtils.loadBip39Credentials("Test123", WalletConfigure.mMnemonic)
    }

    fun checkAuth():Boolean{
        var function = FunctionEncoder.makeFunction("isAuthed",
            arrayListOf("string"), arrayListOf("0x1234") as List<Any>?, arrayListOf("bool"))
        var encodedFunction = FunctionEncoder.encode(function)
        var transaction = Transaction.createEthCallTransaction(
            mWallet?.address.toString(), WalletConfigure.mContractAddress, encodedFunction)
        var response = web3j?.ethCall(transaction, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
        var returnValue = FunctionReturnDecoder.decode(response?.value, function.outputParameters)
        return returnValue[0] as Boolean
    }

    fun requestAuth(){

    }
}