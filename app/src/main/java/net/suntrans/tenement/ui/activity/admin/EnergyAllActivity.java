package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.CompanyInfo;
import net.suntrans.tenement.bean.EnergyListInfo;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.databinding.ActivityEnergyAllBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.activity.EnergyConsumeActivity;
import net.suntrans.tenement.ui.activity.stuff.MyStuffActivity;
import net.suntrans.tenement.ui.activity.stuff.StuffProfileActivity;

import java.util.ArrayList;
import java.util.List;

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
        for (int i=201;i<210;i++){
            EnergyListInfo info = new EnergyListInfo();
            info.name=i+"";
            info.value = "当日用电:"+i+"kW·h";
            datas.add(info);
        }
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refreshLayout.setRefreshing(false);
            }
        });
        adapter = new EnergyAllAdapter(R.layout.item_common, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(EnergyAllActivity.this,EnergyConsumeActivity.class);
                intent.putExtra("title",datas.get(position).name);
                startActivity(intent);
            }
        });
    }

    class EnergyAllAdapter extends BaseQuickAdapter<EnergyListInfo, BaseViewHolder> {

        int imgSize = UiUtils.INSTANCE.dip2px(36);

        public EnergyAllAdapter(int layoutResId, @Nullable List<EnergyListInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EnergyListInfo item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.value, item.value==null?"--":item.value);
            final ImageView toxiang = helper.getView(R.id.touxiang);


        }
    }
}
