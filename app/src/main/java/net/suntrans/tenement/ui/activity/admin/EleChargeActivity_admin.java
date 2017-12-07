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
import net.suntrans.tenement.databinding.ActivityEleChargeAdminBinding;
import net.suntrans.tenement.databinding.ActivityEnergyMoniBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.activity.rent.EleChargeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EleChargeActivity_admin extends BasedActivity {
    private List<Monitor> datas;
    private ActivityEleChargeAdminBinding binding;
    private MoniAdapter adapter;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ele_charge_admin);

        title = getIntent().getStringExtra("title");
        binding.title.setText(title);

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
        adapter = new MoniAdapter(R.layout.item_ele_charge_list, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (title.equals("电费")){
                    Intent intent = new Intent();
                    intent.setClass(EleChargeActivity_admin.this, EleChargeActivity.class);
                    intent.putExtra("title", datas.get(position).name);
                    intent.putExtra("id",datas.get(position).id);
                    intent.putExtra("source","admin");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent();
                    intent.setClass(EleChargeActivity_admin.this, WuyeChargeActivity_admin.class);

                    intent.putExtra("payStatus",datas.get(position).name);
                    startActivity(intent);
                }

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

            if (title.equals("电费")){
                helper.setText(R.id.subName,"上月应缴电费200元");
            }else {
                helper.setText(R.id.subName,"上月应缴物业费200元")
                        .setText(R.id.payStatus,item.name);
                TextView payStatus = helper.getView(R.id.payStatus);
               helper.setTextColor(R.id.payStatus,item.name.equals("已缴纳")?Color.parseColor("#0989fe"):Color.parseColor("#888888"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
       for (int i=0;i<8;i++){
           Monitor monitor  = new Monitor();
           if (i%2==0){
               monitor.name="已缴纳";
           }else {
               monitor.name="未缴纳";

           }
           datas.add(monitor);
           adapter.notifyDataSetChanged();
       }
    }
}
