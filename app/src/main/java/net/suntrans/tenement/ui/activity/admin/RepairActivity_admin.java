package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.FragmentAdapter;
import net.suntrans.tenement.databinding.ActivityRepairAdminBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.MessageFragment;
import net.suntrans.tenement.ui.fragment.admin.RepiarFragmentAdmin;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class RepairActivity_admin extends BasedActivity {

    private ActivityRepairAdminBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repair_admin);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        RepiarFragmentAdmin fragment1 = new RepiarFragmentAdmin();
        adapter.addFragment(fragment1, "d1");
        RepiarFragmentAdmin fragment2 = new RepiarFragmentAdmin();
        adapter.addFragment(fragment2, "d2");
        RepiarFragmentAdmin fragment3 = new RepiarFragmentAdmin();
        adapter.addFragment(fragment3, "d3");
        binding.content.setAdapter(adapter);
        binding.content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    binding.radio0.setChecked(true);
                if (position == 1)
                    binding.radio1.setChecked(true);
                if (position == 2)
                    binding.radio2.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio0:
                        binding.radio0.setChecked(true);
                        break;
                    case R.id.radio1:
                        binding.radio1.setChecked(true);
                        break;
                    case R.id.radio2:
                        binding.radio2.setChecked(true);

                        break;
                }
            }
        });
    }
}
