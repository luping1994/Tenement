package net.suntrans.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.suntrans.tenement.App;
/**
 * Created by Looney on 2017/12/1.
 * Des:
 */

public class NetworkUtils  {

    public static boolean isNetworkAvailable(){
        // 1.获取系统服务
        ConnectivityManager cm = (ConnectivityManager) App.Companion.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 2.获取net信息
        NetworkInfo info = cm.getActiveNetworkInfo();
        // 3.判断网络是否可用
        if (info != null && info.isConnected()) {
           return true;
        } else {
          return false;
        }
    }
}
