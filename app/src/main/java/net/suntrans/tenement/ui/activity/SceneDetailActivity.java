package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivitySceneDetailBinding;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */

public class SceneDetailActivity extends BasedActivity {

    ActivitySceneDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene_detail);
        binding.title.setText(getIntent().getStringExtra("sceneName"));
    }


}
