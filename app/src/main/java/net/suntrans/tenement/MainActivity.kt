package net.suntrans.tenement
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import com.pgyersdk.update.PgyUpdateManager
import net.suntrans.tenement.BuildConfig.DEBUG
import net.suntrans.tenement.databinding.ActivityMainBinding
import net.suntrans.tenement.ui.activity.BasedActivity
import net.suntrans.tenement.ui.fragment.admin.AdminMainFragment
import net.suntrans.tenement.ui.fragment.rent.RentMainFragment
import net.suntrans.tenement.ui.fragment.rent.MineFragment

class MainActivity : BasedActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val role = intent.getIntExtra("role_id", Role.ROLE_RENT_ADMIN)
        when (role) {
            Role.ROLE_RENT_ADMIN -> {
                val rentMainFragment = RentMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, rentMainFragment).commit()
            }

            Role.ROLE_STUFF -> {
                val rentMainFragment = RentMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, rentMainFragment).commit()
            }

            Role.ROLE_TENEMENT_ADMIN -> {
                val adminMainFragment = AdminMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, adminMainFragment).commit()

            }
            else ->{
                val adminMainFragment = AdminMainFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, adminMainFragment).commit()
            }

        }


        if (!DEBUG)
            PgyUpdateManager.register(this, "net.suntrans.tenement.fileProvider")

        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setLogo(R.mipmap.ic_launcher_round)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        var company_name = App.getMySharedPreferences()!!.getString("company_name","")
        supportActionBar!!.setTitle(company_name)
        val mineFragment = MineFragment()
        supportFragmentManager.beginTransaction().replace(R.id.personal,mineFragment).commit()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId==android.R.id.home){
            openDrawerLayout()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        if (!DEBUG)
            PgyUpdateManager.unregister();
        super.onDestroy()
    }


    fun closeDrawerLayout(){
        binding!!.drawerLayout.closeDrawer(Gravity.START)
    }

    fun openDrawerLayout(){
        binding!!.drawerLayout.openDrawer(Gravity.LEFT)
    }

    private val mHits = LongArray(2)
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] >= SystemClock.uptimeMillis() - 2000) {
                //                finish();
                android.os.Process.killProcess(android.os.Process.myPid())
            } else {
                Toast.makeText(this.applicationContext, "再按一次返回键退出", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
