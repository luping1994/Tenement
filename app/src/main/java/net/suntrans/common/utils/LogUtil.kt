package net.suntrans.common.utils

import android.util.Log

import net.suntrans.tenement.BuildConfig.DEBUG


object LogUtil {

    private val TAG = "LogUtil"
    // 是否显示Log消息
    val isShow = DEBUG


    fun i(tag: String, msg: String) {
        if (isShow)
            Log.i(tag, msg)
    }

    fun w(tag: String, msg: String) {
        if (isShow)
            Log.w(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (isShow)
            Log.e(tag, msg)
    }

    fun all(msg: String) {
        if (isShow)
            Log.e("all", msg)
    }

    fun i(msg: String) {
        if (isShow)
            Log.i(TAG, msg)
    }

    fun w(msg: String) {
        if (isShow)
            Log.w(TAG, msg)
    }

    fun e(msg: String) {
        if (isShow)
            Log.e(TAG, msg)
    }

    fun v(msg: String) {
        if (isShow)
            Log.v(TAG, msg)
    }

    fun d(msg: String) {
        if (isShow)
            Log.d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        if (isShow)
            Log.d(tag, msg)
    }

    fun w(tag: String, msg: String, e: Exception) {
        if (isShow)
            Log.w(tag, msg, e)
    }

    fun v(tag: String, msg: String) {
        if (isShow)
            Log.v(tag, msg)
    }
}
