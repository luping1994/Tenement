package net.suntrans.tenement.ui.fragment.rent;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.FragmentRentHomepageBinding;
import net.suntrans.tenement.ui.activity.EnergyConsumeActivity;
import net.suntrans.tenement.ui.activity.SceneActivity;
import net.suntrans.tenement.ui.fragment.ChannelFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RentHomepageFragment extends Fragment {

    private FragmentRentHomepageBinding binding;

    private String[] funName = {"模式", "能耗", "消息", "物业缴费", "报修投诉", "值班表"};
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_mod, R.drawable.ic_energy,
            R.drawable.ic_msg, R.drawable.ic_pay, R.drawable.ic_repair,
            R.drawable.ic_duty};
    private List<Map<String, Object>> datas = new ArrayList<>();

    public RentHomepageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_homepage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //新建适配器
        String[] from = {"image", "name"};
        int[] to = {R.id.image, R.id.name};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), getData(), R.layout.item_home_page_fun, from, to);
        binding.gridLayout.setAdapter(adapter);

        ChannelFragment fragment = new ChannelFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.channelContent, fragment).commit();

        binding.gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(getActivity(), SceneActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), EnergyConsumeActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        });
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("name", funName[i]);
            datas.add(map);
        }

        return datas;

    }
}