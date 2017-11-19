package net.suntrans.tenement.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.App;
import net.suntrans.tenement.MainActivity;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.LoginInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Looney on 2017/7/21.
 */

public class WelcomeActivity extends BasedActivity {

    private String role_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            check();
        }
    }

    private boolean ischeck = false;

    private void check() {
        if (ischeck) {
            return;
        }

        ischeck = true;
        String username = App.Companion.getMySharedPreferences().getString("username", "");
        String password = App.Companion.getMySharedPreferences().getString("password", "");
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            handler.sendEmptyMessageDelayed(START_LOGIN, 2000);
            return;
        }
        mCompositeSubscription.add(api.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<LoginInfo>>(this) {
                    @Override
                    public void onNext(ResultBody<LoginInfo> loginInfoResultBody) {
                        super.onNext(loginInfoResultBody);
                        role_id = loginInfoResultBody.data.user.role_id;
                        App.Companion.getMySharedPreferences().edit()
                                .putString("token", loginInfoResultBody.data.token.access_token)
                                .putString("role_id", role_id)

                                .commit();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                intent.putExtra("role_id",role_id);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        handler.sendEmptyMessageDelayed(START_LOGIN, 2000);
                    }
                }));

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    final int START_LOGIN = 0;
    final int START_MAIN = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_LOGIN) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
            if (msg.what == START_MAIN) {
                System.out.println("startMain");
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }
    };


    public void checkPermission() {
        HiPermission.create(WelcomeActivity.this)
                .checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                       new AlertDialog.Builder(WelcomeActivity.this)
                               .setMessage("您拒绝了存储权限,某些功能可能不可用,是否继续?")
                               .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       check();
                                   }
                               })
                               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       finish();
                                   }
                               }).create().show();
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        System.out.println("guarantee");
                        check();
                    }
                });
    }
}
