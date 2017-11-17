package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityAddStuffBinding;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class AddStuffActivity extends BasedActivity {

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

    }


}
