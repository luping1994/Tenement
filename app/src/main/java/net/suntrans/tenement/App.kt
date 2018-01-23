package net.suntrans.tenement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import com.github.mikephil.charting.utils.Utils
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
        Stetho.initializeWithDefaults(this)

        application = this
        if (!DEBUG) {
            CrashReport.initCrashReport(applicationContext, "62a6fed721", false)
        }

        com.blankj.utilcode.util.Utils.init(this)
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
