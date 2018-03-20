package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.Role;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.WuyeChargeRoom;
import net.suntrans.tenement.databinding.ActivityEleChargeAdminBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.EleChargeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class EleChargeActivity_admin extends BasedActivity {
    private List<WuyeChargeRoom> datas;
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

                EleChargeFragment eleChargeFragment = EleChargeFragment.newInstance(datas.get(position).area_id, datas.get(position).name, Role.ROLE_TENEMENT_ADMIN);
                eleChargeFragment.show(getSupportFragmentManager(), "EleChargeFragment");

            }
        });
    }

    class MoniAdapter extends BaseQuickAdapter<WuyeChargeRoom, BaseViewHolder> {


        public MoniAdapter(int layoutResId, @Nullable List<WuyeChargeRoom> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WuyeChargeRoom item) {
            helper.setText(R.id.name,item.name)
                    .setText(R.id.subName,"在租方:"+item.company_name)
                    .setText(R.id.payStatus,item.pay_type==null?"未知":item.pay_type.equals("0")?"未缴纳":"已缴纳")
                    .setText(R.id.totalMoney,String.format(getString(R.string.dianfei),item.total_money));


            helper.setTextColor(R.id.payStatus,item.pay_type==null?Color.parseColor("#afafaf")
                    :item.pay_type.equals("0")?Color.parseColor("#afafaf")
                    :getResources().getColor(R.color.colorPrimary));
            }
        }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {


        addSubscription(api.getElechargeRoom(), new BaseSubscriber<ResultBody<List<WuyeChargeRoom>>>() {
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
//        addSubscription(RetrofitHelper.getApi().getPayCompanysFee(), new BaseSubscriber<ResultBody<List<ComPayFee>>>(){
//            @Override
//            public void onNext(ResultBody<List<ComPayFee>> listResultBody) {
//               datas.clear();
//               datas.addAll(listResultBody.data);
//               adapter.notifyDataSetChanged();
//               binding.refreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                binding.refreshLayout.setRefreshing(false);
//
//            }
//        });
    }
}
