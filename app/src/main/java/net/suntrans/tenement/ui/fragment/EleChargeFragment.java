package net.suntrans.tenement.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.App;
import net.suntrans.tenement.Constants;
import net.suntrans.tenement.R;
import net.suntrans.tenement.Role;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ElePayInfo;
import net.suntrans.tenement.bean.PayOrder;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmentPayroomBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.widgets.FullScreenDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2018/1/29.
 * Des:
 */

public class EleChargeFragment extends DialogFragment {

    private FragmentPayroomBinding binding;
    private String id;
    private String name;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar ct;
    private IWXAPI wxapi;
    private ElePayInfo data;
    private Subscription subscribe;
    private int source;
    private int role_id;
    private Subscription subscribe1;

    public static EleChargeFragment newInstance(String id, String name,int source) {
        EleChargeFragment fragment = new EleChargeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putInt("source", source);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FullScreenDialog dialog = new FullScreenDialog(getContext(), R.style.transparentDialog);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payroom, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ct = Calendar.getInstance();
        ct.setTime(new Date());
        ct.add(Calendar.MONTH, -1);

        id = getArguments().getString("id");
        name = getArguments().getString("name");
        source = getArguments().getInt("source");

        if (source == Role.ROLE_TENEMENT_ADMIN){
            binding.pay.setText("未缴纳");
        }else {
            binding.pay.setText("缴费");
        }
        role_id = App.Companion.getMySharedPreferences().getInt("role_id", -1);
        binding.name.setText(name);

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String created_at = dateFormat.format(ct.getTime());
//        getData(created_at);

        binding.time.setText(created_at.substring(0, 7));

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct.add(Calendar.MONTH, +1);
                String format = dateFormat.format(ct.getTime());
                binding.time.setText(format.substring(0, 7));
                clearData();
                getData(format);

            }
        });
        binding.pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct.add(Calendar.MONTH, -1);
                String format = dateFormat.format(ct.getTime());
                binding.time.setText(format.substring(0, 7));
                System.out.println(format);
                clearData();
                getData(format);

            }
        });
        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role_id!=Role.ROLE_RENT_ADMIN) {
                    if (role_id==Role.ROLE_STUFF){
                        UiUtils.showToast("您不能缴费哦!");
                        return;
                    }
                    return;
                }
                if (data == null || data.total_money == null) {
                    UiUtils.showToast("获取电费信息,请稍后再试");
                    return;
                }
                if (Float.valueOf(data.total_money) <= 0) {
                    UiUtils.showToast("获取缴费信息失败,无法支付");
                    return;
                }
                payByWX();
            }
        });


        IntentFilter filter  = new IntentFilter();
        filter.addAction("net.suntrans.tenement.pay_success");
        getActivity().registerReceiver(paySuccessReceiver,filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(dateFormat.format(ct.getTime()));
    }

    private Api api = RetrofitHelper.getCookieApi();
    private void getData(String created_at) {
        if (subscribe1!=null){
            if (!subscribe1.isUnsubscribed()){
                subscribe1.unsubscribe();
            }
        }
        binding.pay.setEnabled(false);
        subscribe1 = api.getOrder(id, created_at)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<ElePayInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        UiUtils.showToast("暂无数据");
                        binding.pay.setEnabled(false);
                    }

                    @Override
                    public void onNext(ResultBody<ElePayInfo> body) {

                        data = body.data;

                        if (source==Role.ROLE_TENEMENT_ADMIN) {
                            if (body.data.pay_type.equals("1")) {
                                binding.pay.setEnabled(false);
                                binding.pay.setText("已缴纳");
                            } else {
                                binding.pay.setEnabled(false);
                                binding.pay.setText("未缴纳");
                            }
                        } else {
                            if (body.data.pay_type.equals("1")) {
                                binding.pay.setEnabled(false);
                                binding.pay.setText("已缴纳");
                            } else {
                                binding.pay.setEnabled(true);
                                binding.pay.setText("缴费");
                            }
                        }

                        binding.electrictyAmount.setText(body.data.electricty_amount + "kW·h");
                        binding.totalMoney.setText(body.data.total_money + "元");
                        binding.price.setText(body.data.price + "元/kW·h");
                        binding.electrictyStart.setText(body.data.electricty_start + "kW·h");
                        binding.electrictyEnd.setText(body.data.electricty_end + "kW·h");
                    }
                });
    }

    private void clearData() {
        binding.electrictyAmount.setText("--kW·h");
        binding.totalMoney.setText("--元");
        binding.price.setText("--元/kW·h");
        binding.electrictyStart.setText("--kW·h");
        binding.electrictyEnd.setText("--kW·h");
    }

    public void payByWX() {
        if (subscribe!=null){
            if (!subscribe.isUnsubscribed()){
                subscribe.unsubscribe();
            }
        }

        if (!wxapi.isWXAppInstalled()){
            UiUtils.showToast(getString(R.string.tips_wx_isnotinstall));
            return;
        }
        subscribe = RetrofitHelper.getCookieApi().getWXOrder(data.pay_sn, data.total_money, "android", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBody<PayOrder>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultBody<PayOrder> result) {
                        if (wxapi == null) {
                            wxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID);
                        }
                        System.out.println(result.data.toString());
                        wxapi.registerApp(Constants.APP_ID);
                        PayReq req = new PayReq();

                        req.appId = result.data.appid;  // 测试用appId
                        req.partnerId = result.data.partnerid;
                        req.prepayId = result.data.prepayid;
                        req.nonceStr = result.data.noncestr;
                        req.timeStamp = result.data.timestamp;
                        req.packageValue = result.data.packageX;
                        req.sign = result.data.newsign;

                        wxapi.sendReq(req);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (wxapi != null)
            wxapi.detach();
        if (subscribe!=null){
            if (!subscribe.isUnsubscribed()){
                subscribe.unsubscribe();
            }
        }
        if (subscribe1!=null){
            if (!subscribe1.isUnsubscribed()){
                subscribe1.unsubscribe();
            }
        }

        getActivity().unregisterReceiver(paySuccessReceiver);
        super.onDestroy();
    }


    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData(dateFormat.format(ct.getTime()));

        }
    };

}
