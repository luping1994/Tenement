package net.suntrans.tenement.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.EnergyListItem;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityEnergyListBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.admin.EnergyAllActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/28.
 * Des:
 */

public class EnergyListActivity extends BasedActivity {

    private List<EnergyListItem> datas;
    private ActivityEnergyListBinding binding;
    private EnergyListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_list);
        binding.fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        datas = new ArrayList<>();
        adapter = new EnergyListAdapter(R.layout.item_energy, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();

                intent.setClass(EnergyListActivity.this,EnergyConsumeActivity.class);
                intent.putExtra("title",datas.get(position).name);
                intent.putExtra("id",datas.get(position).id);
                intent.putExtra("monthUsed",datas.get(position).electricity);
                intent.putExtra("todyUsed",datas.get(position).today);
                intent.putExtra("yesterdayUsed",datas.get(position).yesterday);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    class EnergyListAdapter extends BaseQuickAdapter<EnergyListItem, BaseViewHolder> {

        public EnergyListAdapter(int layoutResId, @Nullable List<EnergyListItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EnergyListItem item) {
            helper.setText(R.id.today,item.today+"kWh");
            helper.setText(R.id.yesterday,item.yesterday+"kWh");
            helper.setText(R.id.allPower,item.electricity+"kWh");
            helper.setText(R.id.name,item.name);
        }
    }

    private void getData() {
        addSubscription(api.energyList(), new BaseSubscriber<ResultBody<List<EnergyListItem>>>(this) {
            @Override
            public void onNext(ResultBody<List<EnergyListItem>> listResultBody) {
                super.onNext(listResultBody);
                datas.clear();
                datas.addAll(listResultBody.data);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
