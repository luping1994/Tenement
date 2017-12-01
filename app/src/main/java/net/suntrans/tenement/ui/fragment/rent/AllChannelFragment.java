package net.suntrans.tenement.ui.fragment.rent;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.DeviceType;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneItem;
import net.suntrans.tenement.databinding.FragmentAllChannelBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.widgets.FullScreenDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class AllChannelFragment extends android.support.v4.app.DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        FullScreenDialog dialog = new FullScreenDialog(getContext(),R.style.transparentDialog);
        WindowManager m = getActivity().getWindowManager();

        return dialog;
    }

    private FragmentAllChannelBinding binding;
    private List<ChannelInfo> datas;
    private Myadapter myadapter;
    protected Api api = RetrofitHelper.getApi();
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_channel, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        myadapter = new Myadapter(R.layout.item_choose_channel, datas);
        binding.recyclerview.setAdapter(myadapter);
        binding.refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                datas.get(position).checked = !datas.get(position).checked;
                myadapter.notifyDataSetChanged();
            }
        });
        binding.queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChannelInfo> infos = new ArrayList<>();
                if (onChannelSelectedListener!=null){
                    int count = 0;
                    StringBuilder channel = new StringBuilder();
                    for (ChannelInfo s :
                            datas) {
                        if (s.checked) {
                            count++;
                            channel.append(s.id)
                                    .append(",");
                            infos.add(s);
                        }
                    }
                    if (count < 1) {
                        UiUtils.INSTANCE.showToast("请至少选择一个设备");
                        return;
                    }
                    final String ids = channel.substring(0, channel.length() - 1);

                    onChannelSelectedListener.onChannelSelected(ids);
                    onChannelSelectedListener.onChannelSelected(infos);
                }
                dismiss();

            }
        });

        binding.guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    class Myadapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

        public Myadapter(int layoutResId, @Nullable List<ChannelInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannelInfo item) {
            helper.setText(R.id.name, item.title);
            AppCompatCheckBox checkBox = helper.getView(R.id.checkbox);
            checkBox.setChecked(item.checked);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(DeviceType.deviceIcons.get(item.device_type));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {

        mCompositeSubscription.add(api.getScebeChooseChannel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<ChannelEntity>>(getContext()) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        binding.refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultBody<ChannelEntity> deviceDetailResult) {
                        super.onNext(deviceDetailResult);
                        binding.refreshLayout.setRefreshing(false);
                        datas.clear();
                        datas.addAll(deviceDetailResult.data.lists);
                        myadapter.notifyDataSetChanged();
                    }
                }));
    }


    private onChannelSelectedListener onChannelSelectedListener;

    public void setOnChannelSelectedListener(AllChannelFragment.onChannelSelectedListener onChannelSelectedListener) {
        this.onChannelSelectedListener = onChannelSelectedListener;
    }

    public interface onChannelSelectedListener {
        void onChannelSelected(List<ChannelInfo> items);
        void onChannelSelected(String ids);
    }
}
