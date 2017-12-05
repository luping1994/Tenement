package net.suntrans.tenement.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.NoticeEntity;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmentMessageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.rent.MessageDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.suntrans.tenement.MessageType.messageColor;
import static net.suntrans.tenement.MessageType.messageTips;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class MessageFragment extends BasedFragment {
    private List<NoticeEntity.NoticeItem> datas;
    private FragmentMessageBinding binding;
    private Myadapter myadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();

        myadapter = new Myadapter(R.layout.item_message, datas);
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("url", datas.get(position).url);
                startActivity(intent);
            }
        });
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.recyclerView.setAdapter(myadapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    class Myadapter extends BaseQuickAdapter<NoticeEntity.NoticeItem, BaseViewHolder> {

        private int usedColor;
        private int unusedColor;

        public Myadapter(int layoutResId, @Nullable List<NoticeEntity.NoticeItem> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeEntity.NoticeItem item) {
            TextView status = helper.getView(R.id.status);
            helper.setText(R.id.time, item.created_at)
                    .setText(R.id.title, item.title);
                status.setBackgroundColor(messageColor.get(item.vtype));
                status.setText(messageTips.get(item.vtype));

        }
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        addSubscription(api.loadNoticeList(), new BaseSubscriber<ResultBody<NoticeEntity>>() {
            @Override
            public void onNext(ResultBody<NoticeEntity> body) {
                datas.clear();
                datas.addAll(body.data.lists);
                myadapter.notifyDataSetChanged();
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }
}
