package net.suntrans.tenement.ui.fragment.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.RepairItem;
import net.suntrans.tenement.databinding.FragmentMessageBinding;
import net.suntrans.tenement.ui.activity.admin.RepairDetailActivity;
import net.suntrans.tenement.ui.activity.rent.MessageDetailActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/22.
 * Des:
 */

public class RepiarFragmentAdmin extends BasedFragment {
    private List<RepairItem> datas;
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
            RepairItem info = new RepairItem();
            datas.add(info);
        }
        myadapter = new Myadapter(R.layout.item_repair, datas);
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(),MessageDetailActivity.class));
            }
        });
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(myadapter);
        myadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), RepairDetailActivity.class));
            }
        });
    }

    class Myadapter extends BaseQuickAdapter<RepairItem, BaseViewHolder> {

        private int usedColor;
        private int unusedColor;

        public Myadapter(int layoutResId, @Nullable List<RepairItem> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, RepairItem item) {


        }
    }
}
