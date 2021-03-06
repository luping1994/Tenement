package net.suntrans.tenement.ui.activity.stuff;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.databinding.ActivityStuffProfileBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.UpLoadImageFragment;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/19.
 */

public class StuffProfileActivity extends BasedActivity implements View.OnClickListener, UpLoadImageFragment.onUpLoadListener {

    private ActivityStuffProfileBinding binding;
    private Stuff stuff;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stuff_profile);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.llTouxiang.setOnClickListener(this);
        binding.llName.setOnClickListener(this);
        binding.llTelephone.setOnClickListener(this);
        binding.deleteStuff.setOnClickListener(this);
        binding.llState.setOnClickListener(this);
        binding.llUserName.setOnClickListener(this);
        id = getIntent().getIntExtra("id", 0) + "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTouxiang:
//                showUploadBottomSheet();
                break;
            case R.id.llName:
//                showModifyNameDialog("请输入姓名");
                break;
            case R.id.llTelephone:
//                showModifyNameDialog("请输入电话");

                break;
            case R.id.deleteStuff:
                new AlertDialog.Builder(this)
                        .setMessage("是否删除该员工?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteStuff(id);
                            }
                        }).setNegativeButton("取消", null)
                        .create().show();
                break;
        }
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

    }

    private void showModifyNameDialog(final String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_name, null, false);
        final TextView text = view.findViewById(R.id.text);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = text.getText().toString();

                        if (TextUtils.isEmpty(s)) {
                            UiUtils.showToast(title);
                            if (title.equals("请输入姓名"))
                                stuff.truename = s;
                            else if (title.equals("请输入电话"))
                                stuff.mobile = s;
                            return;
                        }
                        updateProfile();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void updateProfile() {
        Map<String, String> map = new HashMap<>();
        map.put("truename", stuff.truename);
        map.put("mobile", stuff.mobile);
        map.put("status", "1");
        mCompositeSubscription.add(api.updateStuffProfile(map)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody>(this) {
                    @Override
                    public void onNext(ResultBody resultBody) {
                        super.onNext(resultBody);
                        UiUtils.showToast(resultBody.msg);
                    }
                }));
    }

    private void getProfile(String id) {

        mCompositeSubscription.add(api.getStuffProfile(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<Stuff>>(this) {
                    @Override
                    public void onNext(ResultBody<Stuff> stuffResultBody) {
                        stuff = stuffResultBody.data;
//                        LogUtil.i(stuffResultBody.data.cover);
                        binding.name.setText(stuff.truename);
                        binding.userName.setText(stuff.username);
                        binding.telephone.setText(TextUtils.isEmpty(stuff.mobile)?"--":stuff.mobile);
                        binding.state.setText(stuff.status.equals("0")?"停用":"正常");
                        binding.state.setTextColor(stuff.status.equals("0")? getResources().getColor(R.color.stop):getResources().getColor(R.color.colorPrimary));
                        Glide.with(StuffProfileActivity.this)
                                .load(stuff.cover)
                                .asBitmap()
                                .placeholder(R.drawable.ic_atouxiang)
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

    private void deleteStuff(String id) {
        mCompositeSubscription.add(api.deleteStuff(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody>(this.getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody body) {
                        UiUtils.showToast(body.msg);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }
}
