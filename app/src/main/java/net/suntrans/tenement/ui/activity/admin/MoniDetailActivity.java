package net.suntrans.tenement.ui.activity.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.DeviceType;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.MonitorEntity;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.RoomChannel;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class MoniDetailActivity extends BasedActivity {

    private String room_id;
    private List<RoomChannel> datas;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_moni_detail);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        datas = new ArrayList<>();
        room_id = getIntent().getStringExtra("id");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        myadapter = new Myadapter(R.layout.item_device_moni_detail, datas);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myadapter);
        refreshLayout = findViewById(R.id.refreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    public void rightSubTitleClicked(View view) {

    }

    class Myadapter extends BaseQuickAdapter<RoomChannel, BaseViewHolder> {

        private int usedColor;
        private int unusedColor;

        public Myadapter(int layoutResId, @Nullable List<RoomChannel> data) {
            super(layoutResId, data);
            usedColor = getResources().getColor(R.color.colorPrimary);
            unusedColor = getResources().getColor(R.color.enenry_value_textcolr);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomChannel item) {
            helper.setText(R.id.name, item.title == null ? "--" : item.title);
            helper.setText(R.id.des, item.name);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(DeviceType.deviceIcons.get(item.device_type));
            helper.setText(R.id.used,"电流:"+item.current+"A");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (room_id == null)
            return;

        mCompositeSubscription.add(api.loadMonitorRoomChannel(room_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<MonitorEntity>>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultBody<MonitorEntity> deviceDetailResult) {
                        super.onNext(deviceDetailResult);
                        refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(deviceDetailResult.data.lists);
                        myadapter.notifyDataSetChanged();
                    }
                }));
    }
}
