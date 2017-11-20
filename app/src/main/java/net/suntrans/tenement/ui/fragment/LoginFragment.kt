package net.suntrans.tenement.ui.fragment


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.suntrans.common.utils.UiUtils
import net.suntrans.looney.widgets.LoadingDialog
import net.suntrans.tenement.App
import net.suntrans.tenement.MainActivity
import net.suntrans.tenement.R
import net.suntrans.tenement.bean.LoginInfo
import net.suntrans.tenement.bean.ResultBody
import net.suntrans.tenement.databinding.FragmentLoginBinding
import net.suntrans.tenement.rx.BaseSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


/**
 * login fragment
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 * Create by Looney on 2017/11/6
 */
class LoginFragment : BasedFragment() {
    var binding: FragmentLoginBinding? = null

    var dialog:LoadingDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val username = App.getMySharedPreferences()!!.getString("username", "")
        val password = App.getMySharedPreferences()!!.getString("password", "")
        binding!!.account.setText(username)
        binding!!.password.setText(password)
        binding!!.login.setOnClickListener({
            login()
        })
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    fun login() {
        val username = binding!!.account.text.toString()
        val password = binding!!.password.text.toString()

        if (TextUtils.isEmpty(username)) {
            UiUtils.showToast(activity.applicationContext, "请输入用户名")
            return
        }
        if (TextUtils.isEmpty(password)) {
            UiUtils.showToast(activity.applicationContext, "请输入密码")
            return
        }

        if (dialog==null)
        {
            dialog = LoadingDialog(context)
        }
        dialog!!.setCancelable(false)
        dialog!!.setWaitText("登录中,请稍后。。。")
        dialog!!.show()
        binding!!.login.isClickable = false
        mCompositeSubscription.add(api.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<ResultBody<LoginInfo>>(activity.applicationContext) {
                    override fun onNext(loginInfoResponse: ResultBody<LoginInfo>) {
                        super.onNext(loginInfoResponse)
                        dialog!!.dismiss()
                        binding!!.login.isClickable = true
                        App.getMySharedPreferences()!!.edit()
                                .putString("token", loginInfoResponse.data.token.access_token)
                                .putString("username",username)
                                .putString("password",password)
                                .putString("role_id",loginInfoResponse.data.user.role_id)
                                .putString("manager",loginInfoResponse.data.user.manager)
                                .commit()
                        val intent = Intent(activity,MainActivity::class.java)
                        intent!!.putExtra("role_id",loginInfoResponse.data.user.role_id)
                        startActivity(intent)
                        activity.finish()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        e.printStackTrace()
                        dialog!!.dismiss()
                        binding!!.login.isClickable = true

                    }
                }))

    }

}
