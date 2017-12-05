package net.suntrans.tenement.ui.activity.stuff;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityAddStuffBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class AddStuffActivity extends BasedActivity implements View.OnClickListener {

    private ActivityAddStuffBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_stuff);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.addStuff.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        String username = binding.account.getText().toString();
        String password = binding.password.getText().toString();
        String name =binding.name.getText().toString();
        String telephone =binding.telephone.getText().toString();
        addStuff(username,password,name,telephone);
    }

    protected void addStuff(String username, String password, String name, String telephone) {
        if (TextUtils.isEmpty(username)) {
            UiUtils.showToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            UiUtils.showToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast("名字不能为空");
            return;
        }
        if (username.length() < 6) {
            binding.account.setError("长度不能小于6");
            return;

        }
        if (password.length() < 6) {
            binding.password.setError("长度不能小于6");
            return;

        }
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("truename", name);
        if (TextUtils.isEmpty("telephone")) {
            map.put("mobile", "telephone");
        }
        addSubscription(api.addStuff(map), new BaseSubscriber<ResultBody>(this) {
            @Override
            public void onNext(ResultBody resultBody) {
                UiUtils.showToast(resultBody.msg);
            }
        });
    }
}
