package com.example.authdappdemo.perfermance

import org.web3j.crypto.Credentials
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameterName
import java.math.BigInteger

class CurrentNonceTool {
    companion object{
        fun getCurrentNonce(web3jPrv : Admin?, wallet: Credentials?) : BigInteger? {
            return web3jPrv?.ethGetTransactionCount(
                wallet?.address.toString(), DefaultBlockParameterName.LATEST
            )?.sendAsync()?.get()?.transactionCount
        }

        fun getCurrentNonce2(web3jPrv : Admin?, wallet: Credentials?) : BigInteger? {
            return web3jPrv?.ethGetTransactionCount(
                wallet?.address.toString(), DefaultBlockParameterName.PENDING
            )?.sendAsync()?.get()?.transactionCount
        }
    }

}