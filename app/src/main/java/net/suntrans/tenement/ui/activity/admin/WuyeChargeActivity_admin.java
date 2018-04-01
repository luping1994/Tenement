package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.WuyePayInfo;
import net.suntrans.tenement.databinding.ActivityWuyechargeAdminBinding;
import net.suntrans.tenement.databinding.ActivityWuyechargeBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Looney on 2017/12/6.
 * Des:
 */

public class WuyeChargeActivity_admin extends BasedActivity {

    private ActivityWuyechargeAdminBinding binding;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar c;
    private String id;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long maxTime;
    private Api api;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wuyecharge_admin);

        String payStatus = getIntent().getStringExtra("payStatus");
        binding.payStatus.setText(payStatus);
        id = getIntent().getStringExtra("id");


        binding.title.setText(getIntent().getStringExtra("name") + "物业费账单");
        c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String time = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
        getData(id, time);
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
                        new CompatDatePickerDialog(WuyeChargeActivity_admin.this, mDateSetListener, mYear, mMonth, mDay);
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

                String time = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
                getData(id, time);

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

                String time = mYear + "-" + mMonth + "-" + mDay + " 00:00:00";
                getData(id, time);

                settime();
            }
        });

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
        addSubscription(api.getWuyeOrder(area_id, created_at), new BaseSubscriber<ResultBody<WuyePayInfo>>() {
            @Override
            public void onNext(ResultBody<WuyePayInfo> wuyePayInfoResultBody) {
                super.onNext(wuyePayInfoResultBody);

                WuyePayInfo data = wuyePayInfoResultBody.data;
                if (data==null){
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    UiUtils.showToast(getString(R.string.tips_nodata));
                    return;
                }
                binding.totalMoney.setText("总计：" + data.total_money + "元");
                binding.waterFee.setText(data.water_fee + "元");
                binding.publicWaterFee.setText(data.pub_water_fee + "元");
                binding.publicEleFee.setText(data.pub_electricity_fee + "元");
                binding.manageFee.setText(data.management_fee + "元");
                binding.shijian.setText(data.date_ym);
                binding.payStatus.setText(data.pay_type.equals("0") ? "未缴纳" : "已缴纳");
                binding.payStatus.setTextColor(data.pay_type.equals("0") ? Color.parseColor("#ff0000") : getResources().getColor(R.color.colorPrimary));
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
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

}
