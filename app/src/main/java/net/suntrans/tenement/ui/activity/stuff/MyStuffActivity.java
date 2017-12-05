package net.suntrans.tenement.ui.activity.stuff;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import net.suntrans.tenement.databinding.ActivityMystuffBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/19.
 */

public class MyStuffActivity extends BasedActivity {
    private List<Stuff> datas;
    StuffAdapter adapter;
    private ActivityMystuffBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mystuff);
        datas = new ArrayList<>();
        binding.refreshlayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        binding.recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new StuffAdapter(R.layout.item_stuff, datas);
        adapter.bindToRecyclerView(binding.recyclerview);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id",datas.get(position).id);
                intent.setClass(MyStuffActivity.this,StuffProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mCompositeSubscription.add(api.getMyStuff()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<StuffEntity>>(this.getApplicationContext()) {
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

    public void rightSubTitleClicked(View view) {
        Intent intent = new Intent(this, AddStuffActivity.class);
        startActivity(intent);
    }

    class StuffAdapter extends BaseQuickAdapter<Stuff, BaseViewHolder> {

        int imgSize = UiUtils.dip2px(36);

        public StuffAdapter(int layoutResId, @Nullable List<Stuff> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Stuff item) {
            helper.setText(R.id.name, item.truename);
            helper.setText(R.id.mobile, item.mobile==null?"--":item.mobile);
            final ImageView toxiang = helper.getView(R.id.touxiang);
            Glide.with(MyStuffActivity.this)
                    .load(item.cover)
                    .asBitmap()
                    .override(imgSize, imgSize)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            toxiang.setImageBitmap(resource);
                        }
                    });


        }
    }



}
