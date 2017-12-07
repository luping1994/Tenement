package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityElePayBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

/**
 * Created by Looney on 2017/12/5.
 * Des:租户电费缴费页面
 */

public class EleChargeActivity extends BasedActivity {

    private ActivityElePayBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ele_pay);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //以下临时用
        String source = getIntent().getStringExtra("source");
        if (source.equals("admin")){
            binding.pay.setVisibility(View.GONE);
        }

    }
}
