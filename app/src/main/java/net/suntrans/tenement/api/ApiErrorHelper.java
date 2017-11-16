package net.suntrans.tenement.api;

import android.content.Context;

import net.suntrans.common.utils.UiUtils;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiErrorHelper {
    public static void handleCommonError(final Context context, Throwable e) {
        if (e instanceof HttpException) {
            if (e != null) {
                if (e.getMessage() != null) {
                    if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                        UiUtils.INSTANCE.showToast("账号或密码错误");
                    }else {
                        if (e.getMessage() != null)
                            UiUtils.INSTANCE.showToast(e.getMessage());
                    }
                }
            }
        } else if (e instanceof ApiException) {
            int code = ((ApiException) e).code;
            if (code == ApiErrorCode.UNAUTHORIZED) {
//                UiUtils.INSTANCE.showToast("您的身份已过期,请重新登录");
//                Intent intent = new Intent(context, AlertActivity.class);
//                RxBus.getInstance().post(intent);
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.INSTANCE.showToast("网络连接不可用");
            } else if (code == ApiErrorCode.ERROR) {
                UiUtils.INSTANCE.showToast(((ApiException) e).msg);
            } else {
                UiUtils.INSTANCE.showToast(((ApiException) e).msg);
            }
        } else {
            if (e.getMessage() != null)
                UiUtils.INSTANCE.showToast(e.getMessage());
        }
    }
}
