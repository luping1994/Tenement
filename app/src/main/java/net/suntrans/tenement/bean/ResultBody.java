package net.suntrans.tenement.bean;

import net.suntrans.tenement.api.ApiErrorCode;

/**
 * Created by Looney on 2017/9/7.
 */

public class ResultBody<T> {
    public int code;
    public String msg;
    public T data;

    public boolean isOk() {
        return code== ApiErrorCode.OK;
    }
}
