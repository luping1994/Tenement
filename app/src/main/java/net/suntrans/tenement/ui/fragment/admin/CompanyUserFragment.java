package net.suntrans.tenement.ui.fragment.admin;

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
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.databinding.FragmentCompanyEnergyBinding;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyUserFragment extends BasedFragment {

    private FragmentCompanyEnergyBinding binding;
    private List<Stuff> datas;
    private MyUserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_energy,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        for (int i=0;i<8;i++){
            Stuff stuff = new Stuff();
            datas.add(stuff);
        }
        adapter = new MyUserAdapter(R.layout.item_user, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UiUtils.showToast("您没有查看该用户资料的权限");
            }
        });
    }

    private class MyUserAdapter extends BaseQuickAdapter<Stuff, BaseViewHolder> {

        public MyUserAdapter(int layoutResId, @Nullable List<Stuff> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Stuff item) {

        }
    }
}
