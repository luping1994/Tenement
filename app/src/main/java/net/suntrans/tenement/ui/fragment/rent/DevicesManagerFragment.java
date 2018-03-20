package net.suntrans.tenement.ui.fragment.rent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.DeviceEntity;
import net.suntrans.tenement.bean.DeviceInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.DeviceDetailActivity;
import net.suntrans.tenement.ui.activity.EnvDetailActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/4/20.
 */

public class DevicesManagerFragment extends BasedFragment {

    private static final String TAG = "DevicesManagerFragment";
    private DevicesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;


    public static DevicesManagerFragment newInstance() {

        return new DevicesManagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devicesmanager, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        adapter = new DevicesAdapter(datas, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }


    @Override
    public void onResume() {
        getData();
        super.onResume();
    }


    private List<DeviceInfo> datas = new ArrayList<>();

    public static class DevicesAdapter extends RecyclerView.Adapter {
        List<DeviceInfo> datas;
        private Context context;
        public int size = UiUtils.dip2px(96);

        public DevicesAdapter(List<DeviceInfo> datas, Context context) {
            this.datas = datas;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.item_device, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).setData(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            ImageView imageView;
            TextView title;
            TextView name;
            TextView des;
            RelativeLayout root;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                name = (TextView) itemView.findViewById(R.id.name);
                des = (TextView) itemView.findViewById(R.id.des);
                root = (RelativeLayout) itemView.findViewById(R.id.root);


                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("4300".equals(datas.get(getAdapterPosition()).vtype)
                                || "4100".equals(datas.get(getAdapterPosition()).vtype)
                                || "4900".equals(datas.get(getAdapterPosition()).vtype)) {
                            Intent intent = new Intent(context, DeviceDetailActivity.class);
                            intent.putExtra("title", datas.get(getAdapterPosition()).title);
                            intent.putExtra("id", datas.get(getAdapterPosition()).id);
                            context.startActivity(intent);//fc561f
                        } else if ("6100".equals(datas.get(getAdapterPosition()).vtype)) {
                            Intent intent = new Intent(context, EnvDetailActivity.class);
                            intent.putExtra("id", datas.get(getAdapterPosition()).id);
                            intent.putExtra("name",datas.get(getAdapterPosition()).name);
                            context.startActivity(intent);
                        }

                    }
                });

            }

            public void setData(int position) {
                des.setText(datas.get(position).name);
                name.setText(datas.get(position).title);
                if (datas.get(position).is_online.equals("1")) {
                    title.setText("在线");
                    title.setTextColor(Color.parseColor("#1e81d2"));

                } else {
                    title.setText("不在线");
                    title.setTextColor(Color.parseColor("#999999"));
                }

                if ("4300".equals(datas.get(getAdapterPosition()).vtype)) {
                    imageView.setImageResource(R.drawable.ic_4300);
                } else if ("4100".equals(datas.get(getAdapterPosition()).vtype)) {
                    imageView.setImageResource(R.drawable.ic_4100);

                } else if ("6100".equals(datas.get(getAdapterPosition()).vtype)) {
                    imageView.setImageResource(R.drawable.ic_6100);
                } else if ("9100".equals(datas.get(getAdapterPosition()).vtype)) {
                    imageView.setImageResource(R.drawable.ic_9100);
                }
            }
        }
    }

    public void getData() {
        mCompositeSubscription.add(api.getMyDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<DeviceEntity>>(getActivity()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultBody<DeviceEntity> deviceInfoResult) {
                        if (refreshLayout != null)
                            refreshLayout.setRefreshing(false);
                        if (deviceInfoResult != null) {
                            datas.clear();
                            if (deviceInfoResult.data.lists != null) {
                                datas.addAll(deviceInfoResult.data.lists);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        if (datas.size() == 0) {
                            UiUtils.showToast("暂无数据");
                        }
                    }
                }));

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            LogUtil.i(TAG + "可见");
        } else {
            LogUtil.i(TAG + "不可见");

        }
    }
}
