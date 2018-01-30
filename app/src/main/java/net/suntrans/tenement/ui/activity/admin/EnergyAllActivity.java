package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.EnergyListInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityEnergyAllBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.activity.EnergyConsumeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EnergyAllActivity extends BasedActivity {
    private List<EnergyListInfo> datas;
    private ActivityEnergyAllBinding binding;
    private EnergyAllAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_all);
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
                getData(false);
            }
        });
        adapter = new EnergyAllAdapter(R.layout.item_common, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(EnergyAllActivity.this, EnergyConsumeActivity.class);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("id", datas.get(position).id);
                intent.putExtra("monthUsed", datas.get(position).electricity);
                intent.putExtra("todyUsed", datas.get(position).today);
                intent.putExtra("yesterdayUsed", datas.get(position).yesterday);
                startActivity(intent);
            }
        });
        getData(true);
    }

    class EnergyAllAdapter extends BaseQuickAdapter<EnergyListInfo, BaseViewHolder> {

        int imgSize = UiUtils.dip2px(36);

        public EnergyAllAdapter(int layoutResId, @Nullable List<EnergyListInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EnergyListInfo item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.today == null ? "--" : "当日用电:" + item.today + "kW·h");
            final ImageView toxiang = helper.getView(R.id.touxiang);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData(boolean isFrist) {
        if (isFrist) {
            binding.loadingProgress.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.INVISIBLE);

        }
        addSubscription(api.loadEnergyArea(), new BaseSubscriber<ResultBody<List<EnergyListInfo>>>(this) {
            @Override
            public void onNext(ResultBody<List<EnergyListInfo>> listResultBody) {
                datas.clear();
                datas.addAll(listResultBody.data);
                adapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);
                binding.loadingProgress.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                binding.loadingProgress.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.recyclerView.setVisibility(View.INVISIBLE);

            }
        });
        addSubscription(api.loadEnergySummary(), new BaseSubscriber<ResultBody<Map<String, String>>>(this) {
            @Override
            public void onNext(ResultBody<Map<String, String>> body) {
                Map<String, String> data = body.data;
                binding.monthUsed.setText(data.get("month"));
                binding.todayUsedValue.setText(data.get("today") + "kW·h");
                binding.yesterdayUsedValue.setText(data.get("yesterday") + "kW·h");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
