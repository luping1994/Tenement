package net.suntrans.tenement.rx;

import android.content.Context;
import android.util.Log;


import net.suntrans.common.utils.NetworkUtils;
import net.suntrans.tenement.api.ApiErrorCode;
import net.suntrans.tenement.api.ApiErrorHelper;
import net.suntrans.tenement.api.ApiException;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/6.
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    public BaseSubscriber() {
    }

    @Override
    public void onStart() {
        if (!NetworkUtils.isNetworkAvailable()) {
            Log.d("netWork","网络连接不可用");
            this.onError(new ApiException(ApiErrorCode.ERROR_NO_INTERNET, "network interrupt"));
            this.unsubscribe();
            return;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ApiErrorHelper.handleCommonError(context, e);
    }

    @Override
    public void onNext(T t) {

    }
}
