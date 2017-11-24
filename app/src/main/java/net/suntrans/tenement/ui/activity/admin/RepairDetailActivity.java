package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.suntrans.tenement.R;
import net.suntrans.tenement.ui.activity.BasedActivity;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class RepairDetailActivity extends BasedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_repair_detail);
    }
}
