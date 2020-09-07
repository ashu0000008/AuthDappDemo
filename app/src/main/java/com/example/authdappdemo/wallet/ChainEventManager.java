package com.example.authdappdemo.wallet;

import android.app.Activity;
import android.util.Log;

import com.lzh.easythread.EasyThread;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ChainEventManager {
    private static ChainEventManager instance = new ChainEventManager();
    private Disposable mDisposable;
    private EasyThread easyThread = EasyThread.Builder.createScheduled(5).build();

    private ChainEventManager() {
    }

    public static ChainEventManager getInstance() {
        return instance;
    }

    public void startListening(Activity activity, Admin web3jPrv, String contractAddress) {

        Event event = new Event("authedEventFinal",
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));

        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST, contractAddress);
        filter.addSingleTopic(EventEncoder.encode(event));

        easyThread.execute(() -> {
            mDisposable = web3jPrv.ethLogFlowable(filter).subscribe(log -> {

                Log.e("event", log.toString());

                List<Type> args = FunctionReturnDecoder.decode(log.getData(), event.getParameters());
                String contractId = (String) args.get(0).getValue();
                String deviceId = (String) args.get(1).getValue();

                if (null != activity) {
                    activity.runOnUiThread(() -> {
                        WalletManagerPrivateEthereumExtension.INSTANCE.doEventResponse(activity, contractId, deviceId);
                    });
                }
            });
        });
    }

}
