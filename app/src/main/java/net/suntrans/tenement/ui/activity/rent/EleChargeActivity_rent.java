package net.suntrans.tenement.ui.activity.rent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xw.repo.supl.ISlidingUpPanel;
import com.xw.repo.supl.SlidingUpPanelLayout;

import net.suntrans.common.utils.StatusBarCompat;
import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.looney.widgets.cardstack.RxAdapterStack;
import net.suntrans.looney.widgets.cardstack.RxCardStackView;
import net.suntrans.tenement.Constants;
import net.suntrans.tenement.R;
import net.suntrans.tenement.Role;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.PayOrder;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.WeatherModel;
import net.suntrans.tenement.databinding.ActivityElePayBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.EleChargeFragment;
import net.suntrans.tenement.widgets.BaseWeatherPanelView;
import net.suntrans.tenement.widgets.CardPanelView;
import net.suntrans.tenement.widgets.WeatherPanelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.xw.repo.supl.SlidingUpPanelLayout.COLLAPSED;
import static com.xw.repo.supl.SlidingUpPanelLayout.EXPANDED;
import static com.xw.repo.supl.SlidingUpPanelLayout.HIDDEN;

/**
 * Created by Looney on 2017/12/5.
 * Des:租户电费缴费页面
 */

public class EleChargeActivity_rent extends BasedActivity {

    private ActivityElePayBinding binding;

    private int[] colors = {Color.parseColor("#80DEEA"),
            Color.parseColor("#78909C"),
            Color.parseColor("#03A9F4")};
    private MyAdapter adapter;
    private int source;
    private String id;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ele_pay);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        source = getIntent().getIntExtra("source",-1);


        type = getIntent().getStringExtra("type");
        initView();
    }

    private List<WeatherModel> datas = new ArrayList<>();

    private void initView() {
        adapter = new MyAdapter(R.layout.item_payroom, datas);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    getData();
            }
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type.equals("dianfei")){
                    EleChargeFragment eleChargeFragment = EleChargeFragment.newInstance(datas.get(position).id, datas.get(position).name, source);
                    eleChargeFragment.show(getSupportFragmentManager(), "EleChargeFragment");
                }else {
                    Intent intent = new Intent(EleChargeActivity_rent.this,WuyeChargeActivity_rent.class);
                    intent.putExtra("id",datas.get(position).id);
                    intent.putExtra("name",datas.get(position).name);
                    startActivity(intent);
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

            getData();

    }

    private void getData() {

        addSubscription(RetrofitHelper.getApi().getPayRoom(), new BaseSubscriber<ResultBody<List<WeatherModel>>>() {
            @Override
            public void onNext(ResultBody<List<WeatherModel>> datas) {
                super.onNext(datas);
                loadData(datas.data);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getDataByID() {

        addSubscription(RetrofitHelper.getApi().getPayRoom(id), new BaseSubscriber<ResultBody<List<WeatherModel>>>() {
            @Override
            public void onNext(ResultBody<List<WeatherModel>> datas) {
                super.onNext(datas);
                loadData(datas.data);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (binding.refreshLayout != null) {
                    binding.refreshLayout.setRefreshing(false);
                }
            }
        });

    }


    private void loadData(List<WeatherModel> data) {
        datas.clear();
        datas.addAll(data);
        adapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseQuickAdapter<WeatherModel, BaseViewHolder> {

        public MyAdapter(int layoutResId, @Nullable List<WeatherModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WeatherModel item) {
            helper.setText(R.id.name, item.name);
        }
    }


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

}
