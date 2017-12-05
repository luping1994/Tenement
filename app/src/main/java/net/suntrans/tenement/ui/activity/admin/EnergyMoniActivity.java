package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.Monitor;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityEnergyMoniBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EnergyMoniActivity extends BasedActivity {
    private List<Monitor> datas;
    private ActivityEnergyMoniBinding binding;
    private MoniAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_moni);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        datas = new ArrayList<>();

        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getData();
            }
        });
        adapter = new MoniAdapter(R.layout.item_moni, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(EnergyMoniActivity.this, MoniDetailActivity.class);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("id",datas.get(position).id);
                startActivity(intent);
            }
        });
    }

    class MoniAdapter extends BaseQuickAdapter<Monitor, BaseViewHolder> {

        int imgSize = UiUtils.dip2px(36);

        public MoniAdapter(int layoutResId, @Nullable List<Monitor> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Monitor item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.power_rate,item.power+"kW");
            helper.setText(R.id.current,item.current+"A");
            TextView textView = helper.getView(R.id.status);
            textView.setText(item.label.value);
            textView.setBackgroundColor(Color.parseColor(item.label.color));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        addSubscription(api.loadMonitorIndex(), new BaseSubscriber<ResultBody<List<Monitor>>>(this) {
            @Override
            public void onNext(ResultBody<List<Monitor>> listResultBody) {
                super.onNext(listResultBody);
                datas.clear();
                datas.addAll(listResultBody.data);
                adapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                binding.refreshLayout.setRefreshing(false);

            }
        });
    }
}
