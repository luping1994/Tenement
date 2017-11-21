package net.suntrans.tenement

import android.databinding.DataBindingUtil
import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import com.pgyersdk.update.PgyUpdateManager
import net.suntrans.common.utils.StatusBarCompat
import net.suntrans.tenement.databinding.ActivityMainBinding
import net.suntrans.tenement.ui.activity.BasedActivity
import net.suntrans.tenement.ui.fragment.admin.AdminMainFragment
import net.suntrans.tenement.ui.fragment.rent.RentMainFragment

class MainActivity : BasedActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val role = intent.getIntExtra("role_id",Role.ROLE_RENT_ADMIN)
        when (role) {
            Role.ROLE_RENT_ADMIN -> {
                val rentMainFragment = RentMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, rentMainFragment).commit()
                println("role = rent")
            }
            Role.ROLE_STUFF-> {
                val rentMainFragment = RentMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, rentMainFragment).commit()
                println("role = rent")
            }
            Role.ROLE_TENEMENT_ADMIN -> {
                val adminMainFragment = AdminMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, adminMainFragment).commit()

            }
        }

//        println(StatusBarCompat.getNavigationBarHeight(this))
//        println(StatusBarCompat.getStatusBarHeight(this))
//
//        val metric = DisplayMetrics()
//        val metric1 = DisplayMetrics()
//        windowManager.defaultDisplay.getRealMetrics(metric)
//        windowManager.defaultDisplay.getMetrics(metric1)
//        val density = metric.density
//        val widthPixels = metric.widthPixels
//        val heightPixels = metric.heightPixels
//        val densityDpi = metric.densityDpi
//        System.out.println("density=" + density);
//        System.out.println("xdensity=" + metric.xdpi);
//        System.out.println("ydensity=" + metric.ydpi);
//        System.out.println("设备真实宽度=" + widthPixels);
//        System.out.println("设备宽度2=" + metric1.widthPixels);
//        System.out.println("设备高度2=" + metric1.heightPixels);
//        System.out.println("设备真实高度=" + heightPixels);
//        System.out.println("设备密度dpi=" + densityDpi);

        PgyUpdateManager.register(this,"net.suntrans.tenement.fileProvider")

    }

    override fun onDestroy() {
        PgyUpdateManager.unregister()
        super.onDestroy()
    }
}
