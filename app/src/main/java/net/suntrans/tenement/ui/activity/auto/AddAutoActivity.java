package net.suntrans.tenement.ui.activity.auto;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityAddAutoBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

/**
 * Created by Looney on 2017/11/19.
 */

public class AddAutoActivity extends BasedActivity {

    private ActivityAddAutoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_auto);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAutoActivity.this, AddAutoByTime.class);
                startActivity(intent);
            }
        });
        binding.chooseDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAutoActivity.this, AddAutoByTime.class);
                startActivity(intent);
            }
        });
    }
}
