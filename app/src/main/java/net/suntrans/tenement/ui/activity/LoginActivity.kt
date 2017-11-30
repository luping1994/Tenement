package net.suntrans.tenement.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.pgyersdk.update.PgyUpdateManager
import net.suntrans.tenement.BuildConfig.DEBUG
import net.suntrans.tenement.R
import net.suntrans.tenement.adapter.FragmentAdapter
import net.suntrans.tenement.databinding.ActivityLoginBinding
import net.suntrans.tenement.ui.fragment.LoginFragment

class LoginActivity : BasedActivity() {
    var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        val loginFragmnet: LoginFragment = LoginFragment.newInstance()
//        val registerFragment: RegisterFragment = RegisterFragment.newInstance()
        val adapter = FragmentAdapter(supportFragmentManager)
        adapter.addFragment(loginFragmnet, getString(R.string.title_login))
//        adapter.addFragment(registerFragment, getString(R.string.title_register))
        binding!!.mViewPager.adapter = adapter
        binding!!.mTabLayout.setupWithViewPager(binding!!.mViewPager)
        if (!DEBUG)
            PgyUpdateManager.register(this, "net.suntrans.tenement.fileProvider")
    }

    override fun onDestroy() {
        try {
            if (!DEBUG)
                PgyUpdateManager.unregister()
        } finally {

        }
        super.onDestroy()
    }
}
