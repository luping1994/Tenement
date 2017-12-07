package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityWuyechargeBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.Calendar;

/**
 * Created by Looney on 2017/12/6.
 * Des:
 */

public class WuyeChargeActivity extends BasedActivity {

    private ActivityWuyechargeBinding binding;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wuyecharge);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        settime();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompatDatePickerDialog datePickerDialog =
                        new CompatDatePickerDialog(WuyeChargeActivity.this,mDateSetListener,mYear,mMonth,mDay);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(System.currentTimeMillis());
                datePickerDialog.hideDay();
                datePickerDialog.show();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.MONTH, 1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);
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
                settime();
            }
        });

    }

    private void settime() {
        binding.time.setText(mYear+"年"+mMonth+"月");
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
                                    .append(mYear).append("月")
                                    .append(pad(mMonth)).append("年")
                    );

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
