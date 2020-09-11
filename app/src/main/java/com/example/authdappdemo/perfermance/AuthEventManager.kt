package com.example.authdappdemo.perfermance

import android.util.Log
import com.example.authdappdemo.wallet.WalletManager.easyThread
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Utf8String
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter

object AuthEventManager {

    private var mDisposable: Disposable? = null
    private var mTimeStart = System.currentTimeMillis()
    private var mCounter = 0

    fun startListening(web3jPrv: Admin?) {
        val event = Event(
            "authedEventFinal",
            listOf<TypeReference<*>>(
                object : TypeReference<Utf8String?>() {},
                object : TypeReference<Utf8String?>() {})
        )
        val filter = EthFilter(
            DefaultBlockParameterName.LATEST,
            DefaultBlockParameterName.LATEST, Configure2.mContractAddress2
        )
        filter.addSingleTopic(EventEncoder.encode(event))

        easyThread.submit{
            mDisposable = web3jPrv?.ethLogFlowable(filter)?.subscribe(
                (Consumer {
                    val args =
                        FunctionReturnDecoder.decode(it.data, event.parameters)
                    val contractId = args[0].value as String
                    val deviceId = args[1].value as String

                    val timeSpend = (System.currentTimeMillis() - mTimeStart)/1000
                    mCounter++
                    Log.e("AuthEventManager", "完成认证数量：${mCounter}-----deviceId:$deviceId---------spend $timeSpend s")
                }),
                (Consumer<Throwable> {
                    Log.e("AuthEventManager", it.toString())
                })
            )
        }

    }
}