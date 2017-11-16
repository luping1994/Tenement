package net.suntrans.tenement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


/**
 * Created by Looney on 2017/2/20.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this

    }

    companion object {
        fun getMySharedPreferences(): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = application!!.getSharedPreferences("tenement", Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        var sharedPreferences: SharedPreferences? = null
        var application: Application? = null
    }


}
