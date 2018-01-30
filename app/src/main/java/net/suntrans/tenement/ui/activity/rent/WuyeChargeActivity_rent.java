package net.suntrans.tenement.ui.activity.rent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.App;
import net.suntrans.tenement.Constants;
import net.suntrans.tenement.R;
import net.suntrans.tenement.Role;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.PayOrder;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.WuyePayInfo;
import net.suntrans.tenement.databinding.ActivityWuyechargeAdminBinding;
import net.suntrans.tenement.databinding.ActivityWuyechargeRentBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/12/6.
 * Des:
 */

public class WuyeChargeActivity_rent extends BasedActivity {

    private ActivityWuyechargeRentBinding binding;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar c;
    private String id;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long maxTime;
    private Api api;
    private LoadingDialog dialog;
    private int role_id;
    private IWXAPI wxapi;
    private Subscription subscribe;
    private WuyePayInfo data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wuyecharge_rent);
        role_id = App.Companion.getMySharedPreferences().getInt("role_id", -1);

        String payStatus = getIntent().getStringExtra("payStatus");
        binding.payStatus.setText(payStatus);
        id = getIntent().getStringExtra("id");


        IntentFilter filter  = new IntentFilter();
        filter.addAction("net.suntrans.tenement.pay_success");
        registerReceiver(paySuccessReceiver,filter);

        binding.title.setText(getIntent().getStringExtra("name") + "物业费账单");
        c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        getCurrentData();
        settime();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        c.add(Calendar.MONTH, -1);
        maxTime = c.getTimeInMillis();
        c.add(Calendar.MONTH, +1);
        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompatDatePickerDialog datePickerDialog =
                        new CompatDatePickerDialog(WuyeChargeActivity_rent.this, mDateSetListener, mYear, mMonth, mDay);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(maxTime);
                datePickerDialog.hideDay();
                datePickerDialog.show();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.MONTH, 2);
                if (c.getTime().compareTo(new Date()) > 0) {
                    c.add(Calendar.MONTH, -2);
                    UiUtils.showToast("本月物业费未出账");
                    return;
                }
                c.add(Calendar.MONTH, -1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);

                getCurrentData();

                settime();
            }
        });
        binding.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.MONTH, -1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);

                getCurrentData();

                settime();
            }
        });


        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role_id!= Role.ROLE_RENT_ADMIN) {
                    if (role_id==Role.ROLE_STUFF){
                        UiUtils.showToast("您当前账号不能缴费哦!");
                        return;
                    }
                    return;
                }
                if (data == null || data.total_money == null) {
                    UiUtils.showToast("获无法取缴费信息,请稍后再试");
                    return;
                }
                if (Float.valueOf(data.total_money) <= 0) {
                    UiUtils.showToast("获取缴费信息失败,无法支付");
                    return;
                }
                payByWX();
            }
        });
    }

    private void getCurrentData() {
        String time = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
        getData(id, time);
    }

    private void settime() {
        binding.time.setText(mYear + "年" + mMonth + "月");
    }

    private CompatDatePickerDialog.OnDateSetListener mDateSetListener =
            new CompatDatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    binding.time.setText(
                            new StringBuilder()
                                    .append(mYear).append("年")
                                    .append(pad(mMonth)).append("月")
                    );
                    try {
                        String time = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
                        getData(id, time);
                        Date parse = format.parse(time);
                        c.setTime(parse);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void getData(String area_id, String created_at) {
        if (api == null)
            api = RetrofitHelper.getApi();
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后...");
            dialog.setCancelable(false);
        }
        dialog.show();
        clearData();
        binding.pay.setEnabled(false);
        addSubscription(api.getWuyeOrder(area_id, created_at), new BaseSubscriber<ResultBody<WuyePayInfo>>() {
            @Override
            public void onNext(ResultBody<WuyePayInfo> wuyePayInfoResultBody) {
                super.onNext(wuyePayInfoResultBody);
                binding.pay.setEnabled(true);

                data = wuyePayInfoResultBody.data;
                if (data ==null){
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    binding.pay.setEnabled(false);
                    UiUtils.showToast(getString(R.string.tips_nodata));
                    return;
                }
                binding.totalMoney.setText("总计：" + data.total_money + "元");
                binding.waterFee.setText(data.water_fee + "元");
                binding.publicWaterFee.setText(data.pub_water_fee + "元");
                binding.publicEleFee.setText(data.pub_electricity_fee + "元");
                binding.manageFee.setText(data.management_fee + "元");
                binding.shijian.setText(data.created_at.substring(0, 7));
                binding.payStatus.setText(data.pay_type.equals("0") ? "未缴纳" : "已缴纳");
                binding.pay.setText(data.pay_type.equals("0") ? "缴费" : "已缴纳");
                binding.pay.setEnabled(data.pay_type.equals("0"));
                binding.payStatus.setTextColor(data.pay_type.equals("0") ? Color.parseColor("#ff0000") : getResources().getColor(R.color.colorPrimary));
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                binding.pay.setEnabled(false);
                UiUtils.showToast(getString(R.string.tips_nodata));
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }

    private void clearData() {
        binding.totalMoney.setText("总计：--" );
        binding.waterFee.setText( "--元");
        binding.publicWaterFee.setText( "--元");
        binding.publicEleFee.setText( "--元");
        binding.manageFee.setText( "--元");
        binding.shijian.setText("--");
        binding.payStatus.setText("--");
        binding.payStatus.setTextColor( getResources().getColor(R.color.colorPrimary));
    }


    public void payByWX() {
        if (wxapi == null) {
            wxapi = WXAPIFactory.createWXAPI(WuyeChargeActivity_rent.this, Constants.APP_ID);
        }
        if (!wxapi.isWXAppInstalled()){
            UiUtils.showToast(getString(R.string.tips_wx_isnotinstall));
            return;
        }
        if (subscribe!=null){
            if (!subscribe.isUnsubscribed()){
                subscribe.unsubscribe();
            }
        }
        binding.pay.setEnabled(false);
        subscribe = RetrofitHelper.getCookieApi().getWXOrder(data.pay_sn, data.total_money, "android", "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBody<PayOrder>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.pay.setEnabled(true);
                        UiUtils.showToast(getString(R.string.tips_getorder_failed));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultBody<PayOrder> result) {
                        if (wxapi == null) {
                            wxapi = WXAPIFactory.createWXAPI(WuyeChargeActivity_rent.this, Constants.APP_ID);
                        }
                        wxapi.registerApp(Constants.APP_ID);
                        PayReq req = new PayReq();

                        req.appId = result.data.appid;  // 测试用appId
                        req.partnerId = result.data.partnerid;
                        req.prepayId = result.data.prepayid;
                        req.nonceStr = result.data.noncestr;
                        req.timeStamp = result.data.timestamp;
                        req.packageValue = result.data.packageX;
                        req.sign = result.data.newsign;
                        binding.pay.setEnabled(true);

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
        unregisterReceiver(paySuccessReceiver);

        super.onDestroy();
    }

    private BroadcastReceiver paySuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCurrentData();
        }
    };

}
