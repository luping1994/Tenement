package net.suntrans.tenement

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.suntrans.tenement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        binding!!.ddd.setOnClickListener(View.OnClickListener { UiUtils.showToast("helloword!") })
    }
}
