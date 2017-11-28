package net.suntrans.tenement.ui.fragment;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.StatusBarCompat;
import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ChannelAreaEntity;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmnetChannelBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static net.suntrans.tenement.DeviceType.deviceIcons;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelFragment extends android.support.v4.app.DialogFragment {
    private List<ChannelInfo> datas;
    private FragmnetChannelBinding binding;
    private ChannelAdapter adapter;
    protected Api api = RetrofitHelper.getApi();
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private String id;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AppCompatDialog dialog = new AppCompatDialog(getContext());
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager m = window.getWindowManager();
            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            //设置dialog的宽度未matchparent,高度为去掉状态栏和actionbar的高度
            window.setLayout((int) (d.getWidth()*0.9),ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置弹出动画
//            window.setWindowAnimations(R.style.dialogWindowAnim);
            //设置gravity
            window.setGravity(Gravity.CENTER);
        }
        dialog.setCancelable(false);
        return dialog;
    }

    public static ChannelFragment newInstance(String id) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
//        adapter.bindToRecyclerView(binding.recyclerView);
//        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == -1) {
                    UiUtils.INSTANCE.showToast(getActivity().getApplicationContext(), "请稍后...");
                    return;
                }
                sendOrder(datas.get(position).id + "", datas.get(position).status == 1 ? "0" : "1");
            }
        });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
        id = getArguments().getString("id");
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getChannelInfo(id);
            }
        }, 500, 5000);
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void getChannelInfo(String id) {
        mCompositeSubscription.add(api.getChannelById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<ChannelAreaEntity>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<ChannelAreaEntity> channelEntityResultBody) {
                        datas.clear();
                        datas.addAll(channelEntityResultBody.data.channels);
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


    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }
}
