package net.suntrans.tenement.ui.fragment.admin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.MainActivity;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SimpleData;
import net.suntrans.tenement.databinding.FragmentAdminHomepageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.DutyActivity;
import net.suntrans.tenement.ui.activity.EnvDetailActivity;
import net.suntrans.tenement.ui.activity.SceneActivity;
import net.suntrans.tenement.ui.activity.admin.CompanyManagerActivity;
import net.suntrans.tenement.ui.activity.admin.EnergyAllActivity;
import net.suntrans.tenement.ui.activity.admin.EnergyMoniActivity;
import net.suntrans.tenement.ui.activity.admin.PaymentActivity_wuye;
import net.suntrans.tenement.ui.activity.admin.PublicControlActivity;
import net.suntrans.tenement.ui.activity.admin.RepairActivity_admin;
import net.suntrans.tenement.ui.activity.rent.MessageActivity;
import net.suntrans.tenement.ui.activity.rent.PaymentActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class AdminHomepageFragment extends BasedFragment {


    private static final int ENERGYMONI_POS = 1;
    private static final int PUBLICCON_POS = 0;
    private static final int SCENE_POS = 4;
    private static final int ENERGYALL_POS = 2;
    private static final int REPAIR_POS = 7;
    private static final int DUTY_POS = 8;
    private static final int PAYMENT_POS = 3;
    private static final int MESSAGE_POS = 6;
    private static final int COMPANYMANAGER_POS = 5;


    private FragmentAdminHomepageBinding binding;

    private List<SimpleData> datas;
    private String[] funName;
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_deng, R.drawable.ic_jiankong, R.drawable.ic_nenghaofenxi,
                    R.drawable.ic_jiaofei, R.drawable.ic_mode_large, R.drawable.ic_company,
                    R.drawable.ic_gonggao, R.drawable.ic_fuwu, R.drawable.ic_zhibanbiao};
    private EnvInfo envInfo;

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
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.getActivity().registerReceiver(receiver, filter);//注册网络监听广播


        funName = getResources().getStringArray(R.array.admin_page_item_title);
        SimpleAdapter adapter = new SimpleAdapter(R.layout.item_home_page_fun_admin, getData());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case ENERGYMONI_POS:
                        Intent intent = new Intent(getActivity(), EnergyMoniActivity.class);
                        startActivity(intent);
                        break;
                    case PUBLICCON_POS:
                        Intent intent1 = new Intent(getActivity(), PublicControlActivity.class);
                        startActivity(intent1);
                        break;

                    case SCENE_POS:
                        Intent intent2 = new Intent(getActivity(), SceneActivity.class);
                        startActivity(intent2);
                        break;
                    case ENERGYALL_POS:
                        Intent intent3 = new Intent(getActivity(), EnergyAllActivity.class);
                        startActivity(intent3);
                        break;
                    case REPAIR_POS:
                        UiUtils.showToast("该功能如需开通请咨询管理员");
//                        Intent intent4 = new Intent(getActivity(), RepairActivity_admin.class);
//                        startActivity(intent4);
                        break;
                    case DUTY_POS:
                        UiUtils.showToast("该功能如需开通请咨询管理员");
//                        Intent intent5 = new Intent(getActivity(), DutyActivity.class);
//                        startActivity(intent5);
                        break;
                    case PAYMENT_POS:
                        Intent intent6 = new Intent(getActivity(), PaymentActivity_wuye.class);
                        startActivity(intent6);
                        break;
                    case MESSAGE_POS:
                        Intent intent7 = new Intent(getActivity(), MessageActivity.class);
                        startActivity(intent7);
                        break;
                    case COMPANYMANAGER_POS:
//                        UiUtils.showToast("该功能如需开通请咨询管理员");
                        Intent intent8 = new Intent(getActivity(), CompanyManagerActivity.class);
                        startActivity(intent8);
                        break;
                }
            }
        });
        binding.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (envInfo != null) {
                    Intent intent = new Intent(getActivity(), EnvDetailActivity.class);
                    intent.putExtra("id", envInfo.id);
                    intent.putExtra("name", envInfo.name);
                    startActivity(intent);
                } else {
                    UiUtils.showToast("无法获取环境信息");
                }

            }
        });

        binding.tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

//        binding.toolbar.LOGO.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)getActivity()).openDrawerLayout();
//            }
//        });

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
                        envInfo = info.data;
                        binding.wendu.setText(info.data.wendu.value);
                        binding.shidu.setText(" " + info.data.shidu.value + "%");
                        binding.pm25.setText(" " + info.data.pm25.value + "");


                        binding.wenduEva.setText(info.data.wendu.text);
                        binding.shiduEnv.setText("   " + info.data.shidu.text);
                        binding.pm25Eva.setText(" " + info.data.pm25.text);

//                        System.out.println(info.data.wendu.color);
//                        System.out.println(info.data.shidu.color);
//                        System.out.println(info.data.pm25.color);
                        binding.wenduEva.setTextColor(Color.parseColor(info.data.wendu.color));
                        binding.shiduEnv.setTextColor(Color.parseColor(info.data.shidu.color));
                        binding.pm25Eva.setTextColor(Color.parseColor(info.data.pm25.color));
                    }
                }));
    }

    @Override
    public void onDestroy() {
        this.getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                binding.tips.setVisibility(View.VISIBLE);
            } else {
                if (!activeNetworkInfo.isAvailable() || activeNetworkInfo.isFailover()) {
                    //network is not available
                    binding.tips.setVisibility(View.VISIBLE);

                } else {
                    //network is available
                    binding.tips.setVisibility(View.GONE);

                }
            }
        }
    };

}
