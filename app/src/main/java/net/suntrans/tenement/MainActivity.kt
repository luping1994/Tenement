package net.suntrans.tenement

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.suntrans.tenement.databinding.ActivityMainBinding
import net.suntrans.tenement.ui.fragment.RentMainFragment

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val rentMainFragment = RentMainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.content,rentMainFragment).commit()
    }
}
