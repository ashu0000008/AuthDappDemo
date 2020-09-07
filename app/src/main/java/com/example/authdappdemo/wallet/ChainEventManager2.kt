package com.example.authdappdemo.wallet

import android.app.Activity
import com.example.authdappdemo.wallet.WalletManager.easyThread
import com.example.authdappdemo.wallet.WalletManagerPrivateEthereumExtension.doEventResponse
import io.reactivex.disposables.Disposable
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Utf8String
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log

object ChainEventManager2 {

    private var mDisposable: Disposable? = null

    fun startListening(activity: Activity?, web3jPrv: Admin?, contractAddress: String?) {
        val event = Event(
            "authedEventFinal",
            listOf<TypeReference<*>>(
                object : TypeReference<Utf8String?>() {},
                object : TypeReference<Utf8String?>() {})
        )
        val filter = EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST, contractAddress
        )
        filter.addSingleTopic(EventEncoder.encode(event))
        easyThread.execute(Runnable {
            mDisposable = web3jPrv?.ethLogFlowable(filter)?.subscribe { log: Log ->
                android.util.Log.e("event", log.toString())
                val args =
                    FunctionReturnDecoder.decode(log.data, event.parameters)
                val contractId = args[0].value as String
                val deviceId = args[1].value as String
                activity?.runOnUiThread {
                    doEventResponse(
                        activity,
                        contractId,
                        deviceId
                    )
                }
            }
        })
    }
}