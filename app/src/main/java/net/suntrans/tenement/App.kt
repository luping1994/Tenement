package net.suntrans.tenement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import net.suntrans.tenement.BuildConfig.DEBUG

/**
 * Created by Looney on 2017/2/20.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
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
