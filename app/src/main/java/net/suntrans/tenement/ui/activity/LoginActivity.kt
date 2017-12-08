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

        supportFragmentManager.beginTransaction()!!.replace(R.id.content,loginFragmnet).commit();
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
