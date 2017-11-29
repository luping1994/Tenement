package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.Ameter;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityParameterBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

/**
 * Created by Looney on 2017/7/31.
 */

public class AmmeterParameterActivity extends BasedActivity {
    private String sno;
    private ActivityParameterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parameter);
        initData();
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.refreshlayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private void initData() {
        sno = getIntent().getStringExtra("sno");
    }

    private void getData() {
        addSubscription(api.loadAmeter(sno),new BaseSubscriber<ResultBody<Ameter>>(this){
            @Override
            public void onNext(ResultBody<Ameter> body) {
                binding.voltage.setText(body.data.voltage+"V");
                binding.current.setText(body.data.current+"A");
                binding.power.setText(body.data.power+"kW");
                binding.powerRate.setText(body.data.power_rate+"");
                binding.electricity.setText(body.data.electricity+"kWh");
                binding.refreshlayout.setRefreshing(false);
                binding.time.setText(body.data.updated_at);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.refreshlayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

}
