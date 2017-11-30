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
import net.suntrans.tenement.bean.EnergyListItem;
import net.suntrans.tenement.databinding.FragmentCompanyEnergyBinding;
import net.suntrans.tenement.ui.activity.EnergyConsumeActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyEnergyFragment extends BasedFragment {

    private FragmentCompanyEnergyBinding binding;
    private List<EnergyListItem> datas;
    private EnergyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_energy,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        adapter = new EnergyListAdapter(R.layout.item_energy, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();

                intent.setClass(getActivity(),EnergyConsumeActivity.class);
                intent.putExtra("title",datas.get(position).name);
                intent.putExtra("id",datas.get(position).id);
                intent.putExtra("monthUsed",datas.get(position).electricity);
                intent.putExtra("todyUsed",datas.get(position).today);
                intent.putExtra("yesterdayUsed",datas.get(position).yesterday);
                startActivity(intent);
            }
        });
    }

    private class EnergyListAdapter extends BaseQuickAdapter<EnergyListItem, BaseViewHolder> {

        public EnergyListAdapter(int layoutResId, @Nullable List<EnergyListItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EnergyListItem item) {
            helper.setText(R.id.today,item.today+"kWh");
            helper.setText(R.id.yesterday,item.yesterday+"kWh");
            helper.setText(R.id.allPower,item.electricity+"kWh");
            helper.setText(R.id.name,item.name);
        }
    }
}
