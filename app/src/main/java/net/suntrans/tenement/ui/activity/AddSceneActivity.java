package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityAddSceneBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.fragment.PicChooseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class AddSceneActivity extends BasedActivity implements PicChooseFragment.onItemChooseListener {

    private ActivityAddSceneBinding binding;
    private LoadingDialog dialog;
    private PicChooseFragment fragment;
    private String imgId = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_scene);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.addDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PicChooseFragment();
                fragment.setOnItemChooseListener(AddSceneActivity.this);
                fragment.show(getSupportFragmentManager(), "choosePic");
            }
        });
    }


    public void rightSubTitleClicked(View view) {
        createScene();
    }

    private void createScene() {
        String name = binding.sceneName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UiUtils.INSTANCE.showToast("名称不能为空");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("保存中...");
            dialog.setCancelable(false);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("image", imgId);
        System.out.println(name + "," + imgId);

        addSubscription(api.createScene(map), new BaseSubscriber<ResultBody>(getApplicationContext()) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                UiUtils.INSTANCE.showToast(resultBody.msg);
                dialog.dismiss();
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onPicChoose(String id, String path) {
        UiUtils.INSTANCE.showToast(id);
        if (fragment != null) {
            fragment.dismiss();
        }
        Glide.with(this)
                .load(path)
                .dontTransform()
                .crossFade()
                .override(UiUtils.INSTANCE.dip2px(64), UiUtils.INSTANCE.dip2px(64))
                .into(binding.sceneImg);
        imgId = id;
    }
}
