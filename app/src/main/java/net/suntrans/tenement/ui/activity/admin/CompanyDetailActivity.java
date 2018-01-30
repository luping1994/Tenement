package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.FragmentAdapter;
import net.suntrans.tenement.databinding.ActivityCompanyDetailBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.admin.CompanyEnergyFragment;
import net.suntrans.tenement.ui.fragment.admin.CompanyInfoFragment;
import net.suntrans.tenement.ui.fragment.admin.CompanyUserFragment;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyDetailActivity extends BasedActivity {

    private ActivityCompanyDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_detail);
        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        CompanyInfoFragment fragment =  CompanyInfoFragment.newInstance(getIntent().getStringExtra("id"));
        CompanyEnergyFragment fragment1 =  CompanyEnergyFragment.newInstance(getIntent().getStringExtra("id"));
        CompanyUserFragment fragment2 =  CompanyUserFragment.newInstance(getIntent().getStringExtra("id"));
        adapter.addFragment(fragment, "one");
        adapter.addFragment(fragment1, "two");
        adapter.addFragment(fragment2, "three");
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    binding.viewPager.setCurrentItem(0);
                }
                if (checkedId == R.id.radio1) {
                    binding.viewPager.setCurrentItem(1);
                }
                if (checkedId == R.id.radio2) {
                    binding.viewPager.setCurrentItem(2);
                }
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.radio0.setChecked(true);
                }
                if (position == 1) {
                    binding.radio1.setChecked(true);
                }
                if (position == 2) {
                    binding.radio2.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
