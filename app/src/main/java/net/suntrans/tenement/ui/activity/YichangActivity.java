package net.suntrans.tenement.ui.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.YichangEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangActivity extends BasedActivity {

    private List<YichangEntity.DataBeanX.ListsBean.DataBean> datas;
    private MyAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang);

        initToolBar();
        init();
    }

    private void init() {
//DiskLruCache
        datas = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(R.layout.item_yicahng, datas);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                delete(datas.get(pos).log_id);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (currentPage > totalPage) {
                    adapter.loadMoreEnd();
                    return;
                }
                getdata(loadMore);
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    private void delete(int id) {

    }

    private void initToolBar() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class MyAdapter extends BaseItemDraggableAdapter<YichangEntity.DataBeanX.ListsBean.DataBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<YichangEntity.DataBeanX.ListsBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, YichangEntity.DataBeanX.ListsBean.DataBean item) {
            helper.setText(R.id.msg, "" + item.name + ",异常类型:" + item.message)
                    .setText(R.id.time, item.updated_at);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata(fristLoad);
    }

    private int currentPage = 1;
    private int fristLoad = 0;
    private int loadMore = 2;

    private int totalPage = 0;

    private void getdata(final int loadtype) {

//        addSubscription(RetrofitHelper.getApi().getYichang(currentPage + ""), new BaseSubscriber<YichangEntity>(this) {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                super.onError(e);
//
//
//                if (loadtype == loadMore)
//                    adapter.loadMoreFail();
//                else{
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    stateView.showRetry();
//                }
//            }
//
//            @Override
//            public void onNext(YichangEntity o) {
//                if (o.code == 200) {
//                    List<YichangEntity.DataBeanX.ListsBean.DataBean> lists = o.data.lists.data;
//                    if (lists == null || lists.size() == 0) {
//                        if (loadtype==fristLoad){
//                            stateView.showEmpty();
//                            recyclerView.setVisibility(View.INVISIBLE);
//                        }else {
//                            adapter.loadMoreFail();
//
//                        }
//
//                    } else {
//                        if (loadtype == loadMore) {
//                            adapter.loadMoreComplete();
//                        } else {
//
//                        }
//                        totalPage  =o.data.lists.total/o.data.lists.per_page+1;
//                        currentPage++;
//                        stateView.showContent();
//                        recyclerView.setVisibility(View.VISIBLE);
//                        datas.addAll(lists);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                } else {
//                    UiUtils.showToast("获取数据失败");
//                }
//            }
//        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
