package net.suntrans.tenement.ui.activity.rent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.Role;
import net.suntrans.tenement.adapter.FragmentAdapter;
import net.suntrans.tenement.databinding.ActivityMessageBinding;
import net.suntrans.tenement.ui.activity.AddMessageActivity;
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


        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        int role_id = App.Companion.getMySharedPreferences().getInt("role_id", 0);
        if (role_id == Role.ROLE_ROOT_ADMIN || role_id == Role.ROLE_TENEMENT_ADMIN) {
            binding.add.setVisibility(View.VISIBLE);
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MessageActivity.this, AddMessageActivity.class);
                    startActivity(intent);
                }
            });
        } else {
           binding. add.setVisibility(View.GONE);
        }

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        MessageFragment fragment1 = new MessageFragment();
        adapter.addFragment(fragment1, "公告");
        binding.viewPager.setAdapter(adapter);


        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
