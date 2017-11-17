package net.suntrans.tenement.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class DeviceDetailActivity extends BasedActivity {

    private String dev_id;
    private List<ChannelInfo> datas;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title =  findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        datas = new ArrayList<>();
        dev_id = getIntent().getStringExtra("id");
        RecyclerView recyclerView =  findViewById(R.id.recyclerview);
        myadapter = new Myadapter(R.layout.item_device_detail, datas);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myadapter);
        refreshLayout =  findViewById(R.id.refreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(DeviceDetailActivity.this, ChannelEditActivity.class);
                intent.putExtra("id", datas.get(position).id);
                System.out.println(datas.get(position).id);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("channel_type", datas.get(position).device_type);
                startActivity(intent);
            }
        });
    }

    class Myadapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

        public Myadapter(int layoutResId, @Nullable List<ChannelInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannelInfo item) {
            helper.setText(R.id.name, item.name == null ? "--" : item.name)
                    .setText(R.id.des, item.datapoint_name == null ? "--" : item.datapoint_name);
            ImageView imageView = helper.getView(R.id.image);
            if ("1".equals(item.device_type)) {
                imageView.setImageResource(R.drawable.ic_light1);
            } else {
                imageView.setImageResource(R.drawable.ic_socket);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (dev_id == null)
            return;
        mCompositeSubscription.add(api.getDeviceChannel(dev_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<ChannelEntity>>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultBody<ChannelEntity> deviceDetailResult) {
                        super.onNext(deviceDetailResult);
                        refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(deviceDetailResult.data.lists);
                        myadapter.notifyDataSetChanged();
                    }
                }));
    }
}
