package net.suntrans.tenement.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmnetChannelBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelFragment extends BasedFragment {
    private List<ChannelInfo> datas;
    private FragmnetChannelBinding binding;
    private ChannelAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmnet_channel, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        adapter = new ChannelAdapter(R.layout.item_channel, datas);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position==-1)
                {
                    UiUtils.INSTANCE.showToast(getActivity().getApplicationContext(), "请稍后...");
                    return;
                }
                sendOrder(datas.get(position).id + "", datas.get(position).status == 1 ? "0" : "1");
            }
        });


    }

    static class ChannelAdapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

        public ChannelAdapter(int layoutResId, @Nullable List<ChannelInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannelInfo item) {
            helper.setText(R.id.name, item.name);
            View view = helper.getView(R.id.image);
            view.setBackgroundResource(item.status == 1 ? R.drawable.bg_device_on : R.drawable.ic_bg_off);
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
        },500,2000);
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
                .subscribe(new BaseSubscriber<ResultBody<ChannelEntity>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<ChannelEntity> channelEntityResultBody) {
                        datas.clear();
                        datas.addAll(channelEntityResultBody.data.lists);
                        adapter.notifyDataSetChanged();
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
            UiUtils.INSTANCE.showToast(getActivity().getApplicationContext(), "请稍后...");
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
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getChannelInfo();
        }
    };

}
