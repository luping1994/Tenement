package net.suntrans.tenement.ui.activity.stuff;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityProfileBinding;
import net.suntrans.tenement.databinding.ActivityStuffProfileBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.UpLoadImageFragment;

/**
 * Created by Looney on 2017/11/19.
 */

public class StuffProfileActivity extends BasedActivity implements View.OnClickListener, UpLoadImageFragment.onUpLoadListener {

    private ActivityStuffProfileBinding binding;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTouxiang:
                showUploadBottomSheet();
                break;
            case R.id.llName:
                break;
            case R.id.llTelephone:
                break;
            case R.id.deleteStuff:
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
}
