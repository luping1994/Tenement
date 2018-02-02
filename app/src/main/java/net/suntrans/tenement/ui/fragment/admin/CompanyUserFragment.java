package net.suntrans.tenement.ui.fragment.admin;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.bean.StuffEntity;
import net.suntrans.tenement.databinding.FragmentCompanyEnergyBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.stuff.MyStuffActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyUserFragment extends BasedFragment {

    private FragmentCompanyEnergyBinding binding;
    private List<Stuff> datas;
    private MyUserAdapter adapter;
    private String id;

    public static CompanyUserFragment newInstance(String id){
        CompanyUserFragment fragment = new CompanyUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_energy,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        id = getArguments().getString("id");

        adapter = new MyUserAdapter(R.layout.item_user, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                UiUtils.showToast("您没有查看该用户资料的权限");
            }
        });

        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private class MyUserAdapter extends BaseQuickAdapter<Stuff, BaseViewHolder> {
        int imgSize = UiUtils.dip2px(36);

        public MyUserAdapter(int layoutResId, @Nullable List<Stuff> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Stuff item) {

            helper.setText(R.id.name,  item.truename==null?"--":item.truename);
            helper.setText(R.id.telephone, item.mobile==null?"--":item.mobile);

            final ImageView toxiang = helper.getView(R.id.touxiang);

            Glide.with(getActivity())
                    .load(item.cover)
                    .asBitmap()
                    .placeholder(R.drawable.ic_atouxiang)
                    .override(imgSize, imgSize)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            toxiang.setImageBitmap(resource);
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mCompositeSubscription.add(api.getMyStuff(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<StuffEntity>>() {
                    @Override
                    public void onNext(ResultBody<StuffEntity> body) {
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
}
