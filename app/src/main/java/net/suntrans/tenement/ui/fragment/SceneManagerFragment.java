package net.suntrans.tenement.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneInfo;
import net.suntrans.tenement.databinding.FragmentSceneBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.SceneDetailActivity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SceneManagerFragment extends BasedFragment {

    private FragmentSceneBinding binding;
    private SceneAdapter adapter;
    private LoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new SceneAdapter(R.layout.item_scene_manager, SceneFragment.datas, getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.deleteScene) {
                    new IosAlertDialog(getContext())
                            .builder()
                            .setMsg("是否删除?")
                            .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteScene(SceneFragment.datas.get(position).id, position);
                                }
                            }).setNegativeButton(getString(R.string.cancel),null).show();
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SceneDetailActivity.class);
                intent.putExtra("sceneName", SceneFragment.datas.get(position).name);
                intent.putExtra("sceneID", SceneFragment.datas.get(position).id);
                intent.putExtra("img", SceneFragment.datas.get(position).image);
                startActivityForResult(intent,2);
            }
        });
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.scene_header_view, null, false);

        adapter.setHeaderView(headerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    static class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        private Context context;

        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data, Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            helper.addOnClickListener(R.id.deleteScene);
            ImageView imageView = helper.getView(R.id.image);
            Glide.with(context)
                    .load(item.image)
                    .centerCrop()
                    .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
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
                        SceneFragment.datas.clear();
                        SceneFragment.datas.addAll(channelEntityResultBody.data.lists);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private void deleteScene(String id, final int position) {
        if (dialog == null) {

            dialog = new LoadingDialog(getActivity());
            dialog.setWaitText(getString(R.string.info_delete_scene));
            dialog.setCancelable(false);
        }

        dialog.show();
        mCompositeSubscription.add(api.deleteScene(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody channelEntityResultBody) {
                        dialog.dismiss();
                        adapter.remove(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();
                    }
                }));
    }
}
