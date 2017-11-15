package net.suntrans.tenement.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter2 implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public MyAxisValueFormatter2() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + "";
    }

//    @Override
//    public int getDecimalDigits() {
//        return 1;
//    }
}
