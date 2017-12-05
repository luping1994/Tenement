package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import net.suntrans.tenement.bean.CompanyEntity;
import net.suntrans.tenement.bean.CompanyInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityMycompanyBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/19.
 */

public class CompanyManagerActivity extends BasedActivity {
    private List<CompanyInfo> datas;
    CompanyAdapter adapter;
    private ActivityMycompanyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mycompany);
        datas = new ArrayList<>();
        adapter = new CompanyAdapter(R.layout.item_company, datas);
        binding.recyclerview.setAdapter(adapter);
        binding.refreshlayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        adapter.bindToRecyclerView(binding.recyclerview);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(CompanyManagerActivity.this, CompanyDetailActivity.class);
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
        mCompositeSubscription.add(api.loadCompany()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<CompanyEntity>>(this.getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<CompanyEntity> body) {
                        datas.clear();
                        datas.addAll(body.data.lists);
                        adapter.notifyDataSetChanged();
                        binding.refreshlayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        binding.refreshlayout.setRefreshing(false);
                    }
                }));
    }

    public void rightSubTitleClicked(View view) {
        Intent intent = new Intent(this, AddCompanyActivity.class);
        startActivity(intent);
    }

    class CompanyAdapter extends BaseQuickAdapter<CompanyInfo, BaseViewHolder> {

        int imgSize = UiUtils.dip2px(36);

        public CompanyAdapter(int layoutResId, @Nullable List<CompanyInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CompanyInfo item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.roomName,item.address);
            TextView status = helper.getView(R.id.status);
            status.setText( item.status.equals("1")?"在租":"退租");
            status.setBackgroundResource(item.status.equals("1")?R.drawable.bg_device_on:R.drawable.ic_bg_off);
            helper.setText(R.id.startTime, item.date_start == null ? "--" : item.date_start + "~" + item.date_end);


        }
    }

}
