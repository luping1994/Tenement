package net.suntrans.tenement.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.pgyersdk.update.PgyUpdateManager
import net.suntrans.tenement.BuildConfig.DEBUG
import net.suntrans.tenement.R
import net.suntrans.tenement.databinding.ActivityLoginBinding
import net.suntrans.tenement.ui.fragment.LoginFragment

class LoginActivity : BasedActivity() {
    var binding: ActivityLoginBinding? = null
    private var screenHeight = 0//屏幕高度
    private var keyHeight = 0 //软件盘弹起后所占高度

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        if (!DEBUG)
            PgyUpdateManager.register(this, "net.suntrans.tenement.fileProvider")
//        if (isFullScreen(this)) {
//            AndroidBug5497Workaround.assistActivity(this)
//        }
//        initView()
//        initEvent()


        supportFragmentManager.beginTransaction().replace(R.id.content,LoginFragment()).commit()
    }

    override fun onResume() {
        super.onResume()

    }


//    private fun initView() {
//        screenHeight = this.resources.displayMetrics.heightPixels //获取屏幕高度
//        keyHeight = screenHeight / 3//弹起高度为屏幕高度的1/3
//    }
//
//
//    fun isFullScreen(activity: Activity): Boolean {
//        return activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
//    }
//
//    private fun initEvent() {
//        /**
//         * 禁止键盘弹起的时候可以滚动
//         */
//        binding!!.scrollView.setOnTouchListener(View.OnTouchListener { v, event -> true })
//        binding!!.scrollView.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
//              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
//            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
//                Log.e("loginActivity", "up------>" + (oldBottom - bottom))
//                val dist:Float = (binding!!.content.bottom - bottom).toFloat()
//                if (dist > 0) {
//                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(binding!!.content, "translationY", 0.0f, -dist)
//                    mAnimatorTranslateY.duration = 300
//                    mAnimatorTranslateY.interpolator = LinearInterpolator()
//                    mAnimatorTranslateY.start()
////                    AnimationUtils.zoomIn(binding!!.header,0.6f,dist)
//                    AnimationUtils.zoomIn(binding!!.header,0.6f,dist,false)
////                    AnimationUtils.zoomIn(binding!!.bg,0.6f,dist,true)
//                }
////                binding!!.service.visibility = View.INVISIBLE
//
//            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
//                Log.e("wenzhihao", "down------>" + (bottom - oldBottom))
//                if (binding!!.content.bottom - oldBottom > 0) {
//                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(binding!!.content, "translationY", binding!!.content.getTranslationY(), 0f)
//                    mAnimatorTranslateY.duration = 300
//                    mAnimatorTranslateY.interpolator = LinearInterpolator()
//                    mAnimatorTranslateY.start()
//                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
////                    AnimationUtils.zoomOut(binding!!.header, 0.6f)
//                    AnimationUtils.zoomOut(binding!!.header, 0.6f,false)
////                    AnimationUtils.zoomOut(binding!!.bg, 0.6f,true)
//                }
//                binding!!.service.visibility = View.VISIBLE
//            }
//        })
//        binding!!.btnLogin.setOnClickListener(View.OnClickListener { })
//
//        binding!!.ivShowPwd.setOnClickListener(View.OnClickListener {
//            if (binding!!.etPassword.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
//                binding!!.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//                binding!!.ivShowPwd.setImageResource(R.drawable.pass_visuable)
//            } else {
//                binding!!.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//                binding!!.ivShowPwd.setImageResource(R.drawable.pass_gone)
//            }
//            val pwd = binding!!.etPassword.text.toString()
//            if (!TextUtils.isEmpty(pwd))
//                binding!!.etPassword.setSelection(pwd.length)
//        })
//    }

    override fun onDestroy() {
        try {
            if (!DEBUG)
                PgyUpdateManager.unregister()
        } finally {

        }
        super.onDestroy()
    }
}
