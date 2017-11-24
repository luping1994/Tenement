package net.suntrans.tenement.ui.fragment;

import android.content.Intent;
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
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.MessageInfo;
import net.suntrans.tenement.databinding.FragmentMessageBinding;
import net.suntrans.tenement.ui.activity.rent.MessageDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class MessageFragment extends BasedFragment {
    private List<MessageInfo> datas;
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
        for (int i = 0; i < 7; i++) {
            MessageInfo info = new MessageInfo();
            datas.add(info);
        }
        myadapter = new Myadapter(R.layout.item_message, datas);
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(),MessageDetailActivity.class));
            }
        });
        binding.recyclerView.setAdapter(myadapter);
    }

    class Myadapter extends BaseQuickAdapter<MessageInfo, BaseViewHolder> {

        private int usedColor;
        private int unusedColor;

        public Myadapter(int layoutResId, @Nullable List<MessageInfo> data) {
            super(layoutResId, data);
            usedColor = getResources().getColor(R.color.colorPrimary);
            unusedColor = getResources().getColor(R.color.enenry_value_textcolr);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageInfo item) {


        }
    }
}
