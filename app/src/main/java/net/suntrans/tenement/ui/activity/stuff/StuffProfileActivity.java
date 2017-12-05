package net.suntrans.tenement.ui.activity.stuff;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.suntrans.common.utils.UiUtils;
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
        id = getIntent().getIntExtra("id",0)+"";
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
                showUploadBottomSheet();
                break;
            case R.id.llName:
                showModifyNameDialog("请输入姓名");
                break;
            case R.id.llTelephone:
                showModifyNameDialog("请输入电话");

                break;
            case R.id.deleteStuff:
                new AlertDialog.Builder(this)
                        .setMessage("是否删除该员工?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteStuff(id);
                            }
                        }).setNegativeButton("取消",null)
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
        TextView text = view.findViewById(R.id.text);
        final String s = text.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(s)) {
                            UiUtils.showToast(title);
                            if (title.equals("请输入姓名"))
                                stuff.truename=s;
                            else if (title.equals("请输入电话"))
                                stuff.mobile=s;
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
        api.updateStuffProfile(map)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody>(this) {
                    @Override
                    public void onNext(ResultBody resultBody) {
                        super.onNext(resultBody);
                        UiUtils.showToast(resultBody.msg);
                    }
                });
    }

    private void getProfile(String id) {

        api.getStuffProfile(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<Stuff>>(this) {
                    @Override
                    public void onNext(ResultBody<Stuff> stuffResultBody) {
                        stuff = stuffResultBody.data;
                        binding.name.setText(stuff.truename);
                        binding.telephone.setText(stuff.mobile);
                    }
                });
    }

    private void deleteStuff(String id){
        mCompositeSubscription.add(api.deleteStuff(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody>(this.getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody body) {
                      UiUtils.showToast(body.msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }
}
