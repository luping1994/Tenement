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

import com.blankj.utilcode.util.CacheUtils;

import net.suntrans.common.utils.GlideCatchUtil;
import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivitySettingBinding;
import net.suntrans.tenement.persistence.AppDatabase;
import net.suntrans.tenement.persistence.User;
import net.suntrans.tenement.persistence.UserDao;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class SettingActivity extends BasedActivity {

    private ActivitySettingBinding binding;
    private long cacheSize;


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

        findCacheFileSize();
    }

    private void findCacheFileSize() {
        File cacheDir = App.Companion.getApplication().getCacheDir();
        File externalCacheDir = App.Companion.getApplication().getExternalCacheDir();
        File[] dirs = new File[2];
        dirs[0] = cacheDir;
        dirs[1] = externalCacheDir;


        cacheSize = 0;
        Observable.from(dirs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })

                .map(new Func1<File, Long>() {
                    @Override
                    public Long call(File file) {
                        long folderSize = 0l;
                        try {
                            folderSize = GlideCatchUtil.getFolderSize(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        cacheSize += folderSize;
                        return cacheSize;
                    }
                })
                .last()
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String cacheSize1 = GlideCatchUtil.getFormatSize(cacheSize);
                        binding.profile.setText(cacheSize1);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        String cacheSize = GlideCatchUtil.getFormatSize(aLong);
                        binding.profile.setText(cacheSize);
                    }
                });


    }

    private void deleteCacheFile() {
        File cacheDir = App.Companion.getApplication().getCacheDir();
        File externalCacheDir = App.Companion.getApplication().getExternalCacheDir();
        File[] dirs = new File[2];
        dirs[0] = cacheDir;
        dirs[1] = externalCacheDir;

        Observable.from(dirs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
                .map(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        System.out.println(file.getAbsolutePath());
                        return GlideCatchUtil.deleteFile(file);
                    }
                })
                .last()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            UiUtils.showToast("删除成功!");
                        } else {
                            UiUtils.showToast("缓存已经被删除!");
                        }
                        findCacheFileSize();
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

            case R.id.profileRL:
                new IosAlertDialog(this).builder()
                        .setMsg("将清除缓存目录数据,包括用户头像等数据,是否继续?")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteCacheFile();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
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
                        UiUtils.showToast(e.getMessage());
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
        final TextView text = view.findViewById(R.id.oldPassword);
        final TextView newPassTx = view.findViewById(R.id.newPassword);
        final TextView repeatTx = view.findViewById(R.id.repet);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setView(view)
                .setPositiveButton("确定", null).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = text.getText().toString();
                String newPass = newPassTx.getText().toString();
                String repeat = repeatTx.getText().toString();
                if (vertifyPassword(oldPassword, newPass, repeat)) {
                    modifyPassword(oldPassword, newPass);
                    alertDialog.dismiss();
                }

            }
        });
    }

    /**
     * 验证密码格式 正确返回true
     *
     * @param oldPassword
     * @param newPass
     * @param repeat
     * @return
     */
    private boolean vertifyPassword(String oldPassword, String newPass, String repeat) {
        if (TextUtils.isEmpty(oldPassword)) {
            UiUtils.showToast("请输入旧密码");
            return false;
        }
        if (TextUtils.isEmpty(newPass)) {
            UiUtils.showToast("请输入新密码");
            return false;
        }

        if (!newPass.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            UiUtils.showToast("新密码必须为字母开头");
            return false;
        }
        if (newPass.length()<6){
            UiUtils.showToast("新密码长度至少为6位");
            return false;
        }
        if (TextUtils.isEmpty(repeat)) {
            UiUtils.showToast("请重新输入新密码");
            return false;
        }
        if (!newPass.equals(repeat)) {
            UiUtils.showToast("新密码不一致");
            return false;
        }
        return true;
    }

    private void modifyPassword(String old, String newPass) {

        Map<String, String> map = new HashMap<>();
        map.put("old_password", old);
        map.put("new_password", newPass);
        addSubscription(api.modifyPassword(map), new BaseSubscriber<ResultBody>() {
            @Override
            public void onNext(ResultBody body) {
                App.Companion.getMySharedPreferences().edit().clear().commit();
                new IosAlertDialog(SettingActivity.this)
                        .builder()
                        .setMsg("修改密码成功,您需要重新登录!")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                                finish();
                            }
                        }).setCancelable(false).show();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

}

