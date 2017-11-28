package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityPublicAreaBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.AreaOrChannelFragment;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */
public class PublicControlActivity extends BasedActivity {

    private ActivityPublicAreaBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_area);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_public_area);
        final AreaOrChannelFragment fragment = new AreaOrChannelFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,fragment)
                .commit();
        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
