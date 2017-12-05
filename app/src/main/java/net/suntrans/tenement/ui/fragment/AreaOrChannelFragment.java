package net.suntrans.tenement.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.AreaInfo;
import net.suntrans.tenement.bean.ChannelAreaEntity;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmnetChannelOrAreaBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.widgets.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.tenement.DeviceType.deviceIcons;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class AreaOrChannelFragment extends BasedFragment {
    private List<ChannelInfo> datas;
    private List<AreaInfo> areaDatas;
    private FragmnetChannelOrAreaBinding binding;
    private ChannelAdapter adapter;
    private AreaAdapter areaAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmnet_channel_or_area, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setNestedScrollingEnabled(false);

    }

    static class ChannelAdapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

        public ChannelAdapter(int layoutResId, @Nullable List<ChannelInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannelInfo item) {
            helper.setText(R.id.name, item.title);
            ImageView view = helper.getView(R.id.image);
            View imageRl = helper.getView(R.id.imageRl);
            view.setImageResource(deviceIcons.get(item.device_type));
            imageRl.setBackgroundResource(item.status == 1 ? R.drawable.bg_device_on : R.drawable.ic_bg_off);
        }
    }

    static class AreaAdapter extends BaseQuickAdapter<AreaInfo, BaseViewHolder> {

        public AreaAdapter(int layoutResId, @Nullable List<AreaInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AreaInfo item) {
            helper.setText(R.id.name,item.name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getChannelInfo();
            }
        }, 500, 3000);
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void getChannelInfo() {
        mCompositeSubscription.add(api.getHomeChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<ChannelAreaEntity>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<ChannelAreaEntity> channelEntityResultBody) {

                        if (channelEntityResultBody.data.total == 1) {
                            if (adapter == null) {
                                datas = new ArrayList<>();
                                adapter = new ChannelAdapter(R.layout.item_channel, datas);
                                FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
                                binding.recyclerView.setLayoutManager(layoutManager);
                                binding.recyclerView.setAdapter(adapter);
                                adapter.bindToRecyclerView(binding.recyclerView);
                                adapter.setEmptyView(R.layout.recyclerview_empty_view);
                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        if (position == -1) {
                                            UiUtils.showToast(getActivity().getApplicationContext(), "请稍后...");
                                            return;
                                        }
                                        sendOrder(datas.get(position).id + "", datas.get(position).status == 1 ? "0" : "1");
                                    }
                                });
                            } else {
                                datas.clear();
                                datas.addAll(channelEntityResultBody.data.channels);
                                adapter.notifyDataSetChanged();
                            }
                        }else if (channelEntityResultBody.data.total == 2){
                            if (areaAdapter==null){
                                areaDatas = new ArrayList<>();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter = new AreaAdapter(R.layout.item_area_two,areaDatas);
                                FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                                binding.recyclerView.setLayoutManager(layoutManager);
                                binding.recyclerView.setAdapter(areaAdapter);
                                areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        ChannelFragment fragment = ChannelFragment.newInstance(areaDatas.get(position).id + "");
                                        fragment.setCancelable(false);
                                        fragment.show(getChildFragmentManager(),"channelFragment");
                                    }
                                });
                            }else {
                                areaDatas.clear();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter.notifyDataSetChanged();
                            }

                        }else  if (channelEntityResultBody.data.total == 3) {
                            if (areaAdapter==null){
                                areaDatas = new ArrayList<>();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter = new AreaAdapter(R.layout.item_area_three,areaDatas);
                                FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
                                binding.recyclerView.setLayoutManager(layoutManager);
                                binding.recyclerView.setAdapter(areaAdapter);
                                areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        ChannelFragment fragment = ChannelFragment.newInstance(areaDatas.get(position).id + "");
                                        fragment.show(getChildFragmentManager(),"channelFragment");
                                    }
                                });
                            }else {
                                areaDatas.clear();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter.notifyDataSetChanged();
                            }
                        }else {
                            if (areaAdapter==null){
                                areaDatas = new ArrayList<>();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter = new AreaAdapter(R.layout.item_area_three,areaDatas);
                                FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
                                binding.recyclerView.setLayoutManager(layoutManager);
                                binding.recyclerView.setAdapter(areaAdapter);
                                areaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        ChannelFragment fragment = ChannelFragment.newInstance(areaDatas.get(position).id + "");
                                        fragment.show(getChildFragmentManager(),"channelFragment");
                                    }
                                });
                            }else {
                                areaDatas.clear();
                                areaDatas.addAll(channelEntityResultBody.data.lists);
                                areaAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    boolean sending = false;

    public void sendOrder(String id, String cmd) {
        if (sending) {
            UiUtils.showToast(getActivity().getApplicationContext(), "请稍后...");
            return;
        }
        sending = true;
        mCompositeSubscription.add(api.switchChannel(id, cmd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<ChannelControlMessage>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<ChannelControlMessage> cmdMsg) {
                        ChannelControlMessage data = cmdMsg.data;
                        for (int i = 0; i < datas.size(); i++) {
                            if (datas.get(i).id == data.id) {
                                datas.get(i).status = data.status;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        sending = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        sending = false;
                    }
                }));
    }

    private Timer timer = new Timer();
}
