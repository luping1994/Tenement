package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.WuyeChargeRoom;
import net.suntrans.tenement.databinding.ActivityWuyeroomBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2018/1/30.
 * Des:
 */

public class WuyeCRoomActivity_admin extends BasedActivity {

    private ActivityWuyeroomBinding binding;
    private Api api;
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wuyeroom);

        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new MyAdapter(R.layout.item_wycr,datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id",datas.get(position).area_id);
                intent.putExtra("name",datas.get(position).name);
                intent.setClass(WuyeCRoomActivity_admin.this,WuyeChargeActivity_admin.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (api == null)
            api = RetrofitHelper.getApi();
        addSubscription(api.getWuyechargeRoom(), new BaseSubscriber<ResultBody<List<WuyeChargeRoom>>>() {
            @Override
            public void onNext(ResultBody<List<WuyeChargeRoom>> listResultBody) {
                super.onNext(listResultBody);
                datas.clear();
                datas.addAll(listResultBody.data);
                adapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }


    private List<WuyeChargeRoom> datas = new ArrayList<>();
    class MyAdapter extends BaseQuickAdapter<WuyeChargeRoom, BaseViewHolder> {

        int imgSize = UiUtils.dip2px(36);

        public MyAdapter(int layoutResId, @Nullable List<WuyeChargeRoom> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WuyeChargeRoom item) {
            helper.setText(R.id.name,item.name)
                    .setText(R.id.subName,"在租方:"+item.company_name)
                    .setText(R.id.payStatus,item.pay_type==null?"未知":item.pay_type.equals("0")?"未缴纳":"已缴纳")
                    .setText(R.id.totalMoney,String.format(getString(R.string.wuyefei),item.total_money));
            helper.setTextColor(R.id.payStatus,item.pay_type==null?Color.parseColor("#afafaf")
                    :item.pay_type.equals("0")?Color.parseColor("#afafaf")
                    :getResources().getColor(R.color.colorPrimary));
        }
    }
}
