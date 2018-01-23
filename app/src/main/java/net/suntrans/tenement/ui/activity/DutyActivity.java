package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityDutyBinding;

import java.util.Calendar;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class DutyActivity extends BasedActivity {

    private ActivityDutyBinding binding;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_duty);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);


        binding.time.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));
        binding.timeSb.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));
        binding.rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompatDatePickerDialog pickerDialog = new CompatDatePickerDialog(DutyActivity.this, mDateSetListener, mYear, mMonth - 1, mDay);
                DatePicker datePicker = pickerDialog.getDatePicker();
                datePicker.setMaxDate(System.currentTimeMillis());
                pickerDialog.show();
            }
        });
    }

    public void rightSubTitleClicked(View view) {

    }

    private CompatDatePickerDialog.OnDateSetListener mDateSetListener =
            new CompatDatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    binding.timeSb.setText(
                            new StringBuilder()
                                    .append(mYear).append("-")
                                    .append(pad(mMonth)).append("-")
                                    .append(pad(mDay))
                    );
                    binding.time.setText(
                            new StringBuilder()
                                    .append(mYear).append("-")
                                    .append(pad(mMonth)).append("-")
                                    .append(pad(mDay))
                    );
                    getData(binding.time.getText().toString());

                }
            };

    @NonNull
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void getData(String id) {


        //从前从前有个人爱你很久
    }


}
