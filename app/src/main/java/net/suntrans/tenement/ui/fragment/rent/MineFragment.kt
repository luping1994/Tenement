package net.suntrans.tenement.ui.fragment.rent

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import net.suntrans.common.utils.UiUtils
import net.suntrans.tenement.App
import net.suntrans.tenement.R
import net.suntrans.tenement.Role
import net.suntrans.tenement.bean.ProfileWraper
import net.suntrans.tenement.bean.ResultBody
import net.suntrans.tenement.databinding.FragmentMineBinding
import net.suntrans.tenement.persistence.AppDatabase
import net.suntrans.tenement.persistence.User
import net.suntrans.tenement.rx.BaseSubscriber
import net.suntrans.tenement.ui.activity.*
import net.suntrans.tenement.ui.activity.auto.AutomationActivity
import net.suntrans.tenement.ui.activity.stuff.MyStuffActivity
import net.suntrans.tenement.ui.fragment.BasedFragment
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

class MineFragment : BasedFragment(), View.OnClickListener {

    private var binding: FragmentMineBinding? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_mine, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val manager = App.getMySharedPreferences()!!.getInt("role_id", Role.ROLE_RENT_ADMIN)

        if (Role.ROLE_RENT_ADMIN == manager) {
            binding!!.myShop.visibility = View.VISIBLE
        } else {
            binding!!.myShop.visibility = View.GONE
        }
        binding!!.linearLayout.setOnClickListener(this)
        binding!!.message.setOnClickListener(this)
        binding!!.myShop.setOnClickListener(this)
        binding!!.contacts.setOnClickListener(this)
        binding!!.feedback.setOnClickListener(this)
        binding!!.aboutThis.setOnClickListener(this)
        binding!!.setting.setOnClickListener(this)
        binding!!.aotoControl.setOnClickListener(this)

        val id = App.Companion.getMySharedPreferences()!!.getInt("id", 0)


        getUserInfoByLocal(id)
    }

    fun getUserInfoByLocal(id:Int) {
        mCompositeSubscription.add(Observable.just(AppDatabase.getInstance(context)
                .userDao())
                .observeOn(Schedulers.io())
                .flatMap { t -> Observable.just(t.getUserById(id)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<User>() {
                    override fun onNext(user: User?) {
                        binding!!.userName.setText(user!!.nickname)
                        binding!!.telephone.setText(user!!.mobile)
                        Glide.with(context)
                                .load(user.cover)
                                .asBitmap()
                                .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                        binding!!.avatar.setImageBitmap(resource)
                                    }
                                })

                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        e!!.printStackTrace()
                    }

                }))
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.linearLayout -> {
                val intent = Intent(activity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.message -> {
                val intent = Intent(activity, YichangActivity::class.java)
                startActivity(intent)
            }
            R.id.myShop -> {

                val intent = Intent(activity, MyStuffActivity::class.java)
                startActivity(intent)
            }
            R.id.contacts -> {

                val intent = Intent(activity, DeviceManagerActivity::class.java)
                startActivity(intent)
            }
            R.id.feedback -> {
                val intent = Intent(activity, FeedbackActivity::class.java)
                startActivity(intent)
            }
            R.id.aboutThis -> {
                val intent = Intent(activity, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent = Intent(activity, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.aotoControl -> {
                val intent = Intent(activity, AutomationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getUserInfo() {
        api.loadUserInfo()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<ResultBody<ProfileWraper>>(context) {
                    override fun onNext(t: ResultBody<ProfileWraper>?) {
                        super.onNext(t)

                        AppDatabase.getInstance(context)
                                .userDao()
                                .updateUser(t!!.data.user)
                        getUserInfoByLocal(t.data.user.id)
//                        throw RuntimeException("test")
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        e!!.printStackTrace()
                    }
                })
    }
}
