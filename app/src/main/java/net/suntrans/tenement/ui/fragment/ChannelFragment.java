package net.suntrans.tenement.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ChannleInfo;
import net.suntrans.tenement.databinding.FragmnetChannelBinding;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelFragment extends Fragment {
    private List<ChannleInfo> datas;
    private FragmnetChannelBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmnet_channel, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
//        BottomNavigationView
        for (int i =0;i<8;i++){
            ChannleInfo info = new ChannleInfo();
            info.name = "dasds";
            datas.add(info);
        }
        ChannelAdapter adapter = new ChannelAdapter(R.layout.item_channel,datas);
        binding.recyclerView.setAdapter(adapter);
    }

    static class ChannelAdapter extends BaseQuickAdapter<ChannleInfo,BaseViewHolder>{

        public ChannelAdapter(int layoutResId, @Nullable List<ChannleInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChannleInfo item) {

        }
    }
}
