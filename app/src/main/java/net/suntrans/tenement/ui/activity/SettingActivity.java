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
        switch (view.getId()){
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
        App.Companion.getMySharedPreferences().edit()
                .putString("password","")
                .putString("token","")
                .putString("role_id","")
                .commit();
        killAll();
        startActivity(new Intent(this,LoginActivity.class));
    }


}

