package net.suntrans.tenement.ui.activity;

import android.os.Bundle;

import net.suntrans.tenement.R;
import net.suntrans.tenement.ui.fragment.rent.DevicesManagerFragment;


/**
 * Created by Looney on 2017/4/20.
 */

public class DeviceManagerActivity extends BasedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemanager);
        DevicesManagerFragment fragment = DevicesManagerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


}
