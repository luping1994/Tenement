package net.suntrans.tenement.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneInfo;
import net.suntrans.tenement.databinding.FragmentSceneBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.AddSceneActivity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SceneFragment extends BasedFragment {


    private List<SceneInfo> datas;
    private FragmentSceneBinding binding;
    private SceneAdapter adapter;
    private QMUITipDialog tipsDialog;
    private static Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreatedView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("onViewCreated");
        datas = new ArrayList<>();
        adapter = new SceneAdapter(R.layout.item_scene, datas,getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                sendOrder(datas.get(position).id);
            }
        });
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.scene_header_view,null,false);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.item_add_scene,null,false);
        adapter.setFooterView(footerView);
        adapter.setHeaderView(headerView);
        footerView.findViewById(R.id.addScene)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), AddSceneActivity.class));
                    }
                });
    }

    @Override
    public void onResume() {

        getData();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    static class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        private Context context;
        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data,Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);
            Glide.with(context)
                    .load(item.img_url)
                    .centerCrop()
                    .override(UiUtils.INSTANCE.dip2px(36),UiUtils.INSTANCE.dip2px(36))
                    .crossFade()
                    .placeholder(R.drawable.ic_sleep)
                    .into(imageView);
        }
    }

    private void getData() {
        mCompositeSubscription.add(api.getScene()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<SceneEntity>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<SceneEntity> channelEntityResultBody) {
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

    public void sendOrder(String id) {
        if (sending) {
            UiUtils.INSTANCE.showToast(getActivity().getApplicationContext(), "请稍后...");
            return;
        }
        sending = true;
        mCompositeSubscription.add(api.switchScene(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody cmdMsg) {
                        UiUtils.INSTANCE.showToast(cmdMsg.msg);
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
}
