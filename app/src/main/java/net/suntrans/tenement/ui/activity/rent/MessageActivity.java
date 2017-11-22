package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.FragmentAdapter;
import net.suntrans.tenement.databinding.ActivityMessageBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.MessageFragment;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class MessageActivity extends BasedActivity {

    private ActivityMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        MessageFragment fragment1 = new MessageFragment();
        adapter.addFragment(fragment1, "公告");
        binding.viewPager.setAdapter(adapter);

        binding.mTabLayout.setupWithViewPager(binding.viewPager);

        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
