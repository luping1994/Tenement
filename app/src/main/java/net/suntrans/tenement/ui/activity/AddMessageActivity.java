package net.suntrans.tenement.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.suntrans.tenement.R;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class AddMessageActivity extends BasedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
