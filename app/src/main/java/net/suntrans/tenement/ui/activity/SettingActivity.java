package net.suntrans.tenement.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivitySettingBinding;
import net.suntrans.tenement.persistence.AppDatabase;
import net.suntrans.tenement.persistence.User;
import net.suntrans.tenement.persistence.UserDao;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class SettingActivity extends BasedActivity {

    private ActivitySettingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signOut:
                new AlertDialog.Builder(this)
                        .setMessage("注销登录")
                        .setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton(R.string.qvxiao, null)
                        .create().show();
                break;
            case R.id.modifyPassword:
                showModifyPasswordDialog();
                break;
        }

    }

    private void signOut() {

        final int id = App.Companion.getMySharedPreferences().getInt("id", 0);
        final User user = new User();
        user.id = id;
        mCompositeSubscription.add(Observable.just(AppDatabase.getInstance(this)
                .userDao())
                .observeOn(Schedulers.io())
                .map(new Func1<UserDao, String>() {
                    @Override
                    public String call(UserDao userDao) {
                        userDao.delete(user);
                        return "ok";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        App.Companion.getMySharedPreferences().edit()
                                .clear()
                                .commit();
                        killAll();
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    }
                }));

    }

    private void showModifyPasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_modify_password, null, false);
        TextView text = view.findViewById(R.id.oldPassword);
        final String s = text.getText().toString();
        new AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(s)) {
                            UiUtils.INSTANCE.showToast("请输入旧密码");
                            return;
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


}

