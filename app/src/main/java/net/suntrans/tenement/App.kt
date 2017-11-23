package net.suntrans.tenement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import com.tencent.bugly.crashreport.CrashReport
import net.suntrans.tenement.BuildConfig.DEBUG

/**
 * Created by Looney on 2017/2/20.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        Stetho.initializeWithDefaults(this)
        if (!DEBUG) {
            CrashReport.initCrashReport(applicationContext, "62a6fed721", false)
        }
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
