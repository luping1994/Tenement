package net.suntrans.tenement.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

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


}

