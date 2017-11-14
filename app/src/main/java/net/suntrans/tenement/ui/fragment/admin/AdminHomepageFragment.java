package net.suntrans.tenement.ui.fragment.admin;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.SimpleData;
import net.suntrans.tenement.databinding.FragmentAdminHomepageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AdminHomepageFragment extends Fragment {

    private FragmentAdminHomepageBinding binding;

    private List<SimpleData> datas;
    private String[] funName;
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_nenghao, R.drawable.ic_deng,
            R.drawable.ic_mode_large, R.drawable.ic_nenghaofenxi, R.drawable.ic_fuwu,
            R.drawable.ic_zhibanbiao,R.drawable.ic_jiaofei,R.drawable.ic_gonggao};

    public AdminHomepageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_homepage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        funName = getResources().getStringArray(R.array.admin_page_item_title);
        SimpleAdapter adapter = new SimpleAdapter(R.layout.item_home_page_fun_admin, getData());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });


    }

    static class SimpleAdapter extends BaseQuickAdapter<SimpleData, BaseViewHolder> {

        public SimpleAdapter(int layoutResId, @Nullable List<SimpleData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SimpleData item) {
            helper.setText(R.id.name,item.name);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.imgResId);
        }
    }

    public List<SimpleData> getData() {
        datas = new ArrayList<>();
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            SimpleData data = new SimpleData(icon[i],funName[i]);

            datas.add(data);
        }

        return datas;

    }
}
