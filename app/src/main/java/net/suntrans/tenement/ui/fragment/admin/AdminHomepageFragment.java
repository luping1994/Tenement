package net.suntrans.tenement.ui.fragment.admin;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SimpleData;
import net.suntrans.tenement.databinding.FragmentAdminHomepageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.DutyActivity;
import net.suntrans.tenement.ui.activity.EnergyConsumeActivity;
import net.suntrans.tenement.ui.activity.SceneActivity;
import net.suntrans.tenement.ui.activity.admin.CompanyManagerActivity;
import net.suntrans.tenement.ui.activity.admin.EnergyAllActivity;
import net.suntrans.tenement.ui.activity.admin.EnergyMoniActivity;
import net.suntrans.tenement.ui.activity.admin.PublicControlActivity;
import net.suntrans.tenement.ui.activity.admin.RepairActivity_admin;
import net.suntrans.tenement.ui.activity.rent.MessageActivity;
import net.suntrans.tenement.ui.activity.rent.PaymentActivity;
import net.suntrans.tenement.ui.activity.rent.RepairActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class AdminHomepageFragment extends BasedFragment {

    private FragmentAdminHomepageBinding binding;

    private List<SimpleData> datas;
    private String[] funName;
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_nenghao, R.drawable.ic_deng,
            R.drawable.ic_mode_large, R.drawable.ic_nenghaofenxi, R.drawable.ic_fuwu,
            R.drawable.ic_zhibanbiao, R.drawable.ic_jiaofei, R.drawable.ic_gonggao, R.drawable.ic_company};

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
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), EnergyMoniActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), PublicControlActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(getActivity(), SceneActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), EnergyAllActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), RepairActivity_admin.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getActivity(), DutyActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getActivity(), PaymentActivity.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getActivity(), MessageActivity.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getActivity(), CompanyManagerActivity.class);
                        startActivity(intent8);
                        break;
                }
            }
        });


    }

    static class SimpleAdapter extends BaseQuickAdapter<SimpleData, BaseViewHolder> {

        public SimpleAdapter(int layoutResId, @Nullable List<SimpleData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SimpleData item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.imgResId);
        }
    }

    public List<SimpleData> getData() {
        datas = new ArrayList<>();
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            SimpleData data = new SimpleData(icon[i], funName[i]);

            datas.add(data);
        }

        return datas;

    }


    @Override
    public void onResume() {
        getEnv();
        super.onResume();
    }

    private void getEnv() {
        mCompositeSubscription.add(api.getHomeEnv()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody<EnvInfo>>(getContext()) {
                    @Override
                    public void onNext(ResultBody<EnvInfo> info) {
                        binding.wendu.setText(info.data.wendu.value);
                        binding.shidu.setText(" " + info.data.shidu.value + "%");
                        binding.pm25.setText(" " + info.data.pm25.value + "");
                    }
                }));
    }

}
