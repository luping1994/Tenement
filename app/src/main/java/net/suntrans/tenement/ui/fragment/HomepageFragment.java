package net.suntrans.tenement.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.FragmentHomepageBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HomepageFragment extends Fragment {

    private FragmentHomepageBinding binding;

    private String[] funName = {"模式", "能耗", "消息", "物业缴费", "报修投诉", "值班表"};
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_mod, R.drawable.ic_energy,
            R.drawable.ic_msg, R.drawable.ic_pay, R.drawable.ic_repair,
            R.drawable.ic_duty};
    private List<Map<String, Object>> datas = new ArrayList<>();
    public HomepageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_homepage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //新建适配器
        String [] from ={"image","name"};
        int [] to = {R.id.image,R.id.name};
        SimpleAdapter adapter = new SimpleAdapter(getContext(),getData(),R.layout.item_home_page_fun,from,to);
        binding.gridLayout.setAdapter(adapter);
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
