package net.suntrans.tenement.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ProfileWraper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityProfileBinding;
import net.suntrans.tenement.persistence.AppDatabase;
import net.suntrans.tenement.persistence.User;
import net.suntrans.tenement.persistence.UserDao;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.fragment.UpLoadImageFragment;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/19.
 */

public class ProfileActivity extends BasedActivity implements View.OnClickListener, UpLoadImageFragment.onUpLoadListener {

    private ActivityProfileBinding binding;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.llTouxiang.setOnClickListener(this);
        binding.llName.setOnClickListener(this);
        binding.llTelephone.setOnClickListener(this);
        if (UiUtils.isNetworkAvailable()) {
            getUserProfile();
        } else {
            getDataFromLocal();
        }
    }

    private void getDataFromLocal() {
        final int id = App.Companion.getMySharedPreferences().getInt("id", 0);
        mCompositeSubscription.add(Observable.just(AppDatabase.getInstance(this)
                .userDao())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<UserDao, Observable<User>>() {
                    @Override
                    public Observable<User> call(UserDao userDao) {
                        User userById = userDao.getUserById(id);
                        return Observable.just(userById);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User info) {
                        user = info;
                        binding.name.setText(user.nickname);
                        binding.telephone.setText(user.mobile);
                        binding.truename.setText(user.truename);

                        Glide.with(ProfileActivity.this)
                                .load(user.cover)
                                .asBitmap()
                                .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        binding.touxiang.setImageBitmap(resource);
                                    }
                                });

                    }
                }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTouxiang:
                showUploadBottomSheet();
                break;
            case R.id.llName:
                showModifyNameDialog();
                break;
            case R.id.llTelephone:
                showModifyMobileDialog();
                break;
        }
    }

    private void showModifyNameDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_name, null, false);
        final TextView text = view.findViewById(R.id.text);
        new AlertDialog.Builder(this)
                .setTitle("修改昵称")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = text.getText().toString();

                        if (TextUtils.isEmpty(s)) {
                            UiUtils.showToast("请输入姓名");
                            return;
                        }
                        user.nickname = s;
                        updateProfile("nickname",s);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void showModifyMobileDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_phone, null, false);
        final TextView text = view.findViewById(R.id.text);
        new AlertDialog.Builder(this)
                .setTitle("修改手机号")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = text.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            UiUtils.showToast("请输入手机号码");
                            return;
                        }
                        user.mobile = s;
                        updateProfile("mobile",s);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    UpLoadImageFragment fragment;

    private void showUploadBottomSheet() {
        fragment = (UpLoadImageFragment) getSupportFragmentManager().findFragmentByTag("bottomSheetDialog");
        if (fragment == null) {
            fragment = UpLoadImageFragment.newInstance(UpLoadImageFragment.SCALE_TYPE_1_1);
            fragment.setCancelable(true);
            fragment.setLoadListener(this);
        }
        fragment.show(getSupportFragmentManager(), "bottomSheetDialog");

    }

    @Override
    public void uploadImageSuccess(String path) {
        user.cover = "http://gzfhq.suntrans-cloud.com/"+path;
        updateProfile("cover",path);
    }

    private void updateProfile(String key,String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);

        mCompositeSubscription.add(api.updateProfile(map)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<ResultBody>() {
                    @Override
                    public void call(ResultBody body) {
                        AppDatabase.getInstance(ProfileActivity.this)
                                .userDao().updateUser(user);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody>(this) {
                    @Override
                    public void onNext(ResultBody resultBody) {
                        super.onNext(resultBody);
                        UiUtils.showToast(resultBody.msg);
                        getDataFromLocal();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                }));
    }

    private void getUserProfile() {
        api.loadUserInfo()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<ResultBody<ProfileWraper>>() {
                    @Override
                    public void call(ResultBody<ProfileWraper> profileWraperResultBody) {
                        AppDatabase.getInstance(ProfileActivity.this)
                                .userDao().updateUser(profileWraperResultBody.data.user);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<ProfileWraper>>(this) {
                    @Override
                    public void onNext(ResultBody<ProfileWraper> result) {


                        user = result.data.user;
                        binding.name.setText(user.nickname);
                        binding.telephone.setText(user.mobile);
                        binding.truename.setText(user.truename);

                        Glide.with(ProfileActivity.this)
                                .load(user.cover)
                                .asBitmap()
                                .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        binding.touxiang.setImageBitmap(resource);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }
}
