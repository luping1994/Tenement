package net.suntrans.tenement.ui.fragment.rent;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmentRentHomepageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.DutyActivity;
import net.suntrans.tenement.ui.activity.EnergyListActivity;
import net.suntrans.tenement.ui.activity.EnvDetailActivity;
import net.suntrans.tenement.ui.activity.SceneActivity;
import net.suntrans.tenement.ui.activity.admin.CompanyManagerActivity;
import net.suntrans.tenement.ui.activity.rent.MessageActivity;
import net.suntrans.tenement.ui.activity.rent.PaymentActivity;
import net.suntrans.tenement.ui.activity.rent.RepairActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;
import net.suntrans.tenement.ui.fragment.AreaOrChannelFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class RentHomepageFragment extends BasedFragment {

    private FragmentRentHomepageBinding binding;

    private String[] funName = {"模式", "能耗", "公告", "物业缴费", "报修投诉", "值班表"};
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_mod, R.drawable.ic_energy,
            R.drawable.ic_msg, R.drawable.ic_pay, R.drawable.ic_repair,
            R.drawable.ic_duty};
    private List<Map<String, Object>> datas = new ArrayList<>();
    private AreaOrChannelFragment fragment;
    private EnvInfo envData;

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

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.getActivity().registerReceiver(receiver, filter);//注册网络监听广播
        //新建适配器
        String[] from = {"image", "name"};
        int[] to = {R.id.image, R.id.name};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), getData(), R.layout.item_home_page_fun, from, to);
        binding.gridLayout.setAdapter(adapter);
        fragment = new AreaOrChannelFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.channelContent, fragment).commit();
        binding.gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), SceneActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), EnergyListActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), MessageActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), PaymentActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), RepairActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getActivity(), DutyActivity.class);
                        startActivity(intent5);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getActivity(), CompanyManagerActivity.class);
                        startActivity(intent8);
                        break;
                }
            }
        });
        binding.refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragment.getChannelInfo();
                getEnv();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        binding.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (envData!=null){
                    Intent intent = new Intent(getActivity(), EnvDetailActivity.class);
                    intent.putExtra("id",envData.id);
                    intent.putExtra("name",envData.name);
                    startActivity(intent);
                }else {
                    UiUtils.INSTANCE.showToast("无法获取环境信息");
                }

            }
        });
        binding.tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        this.getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private Handler handler = new Handler();

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
                        envData = info.data;
                        binding.wendu.setText(info.data.wendu.value+"℃");
                        binding.wenduEva.setText(info.data.wendu.text);
                        binding.shidu.setText(" " + info.data.shidu.value + "%");
                        binding.pm25.setText(" " + info.data.pm25.value + "");

                        binding.shiduEnv.setText("   "+info.data.shidu.text);
                        binding.pm25Eva.setText(" "+info.data.pm25.text);
                    }
                }));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo==null){
               binding.tips.setVisibility(View.VISIBLE);
            }else {
                if (!activeNetworkInfo.isAvailable()||activeNetworkInfo.isFailover()){
                    //network is not available
                    binding.tips.setVisibility(View.VISIBLE);

                }else {
                    //network is available
                    binding.tips.setVisibility(View.GONE);

                }
            }
        }
    };



}
