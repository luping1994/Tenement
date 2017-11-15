package net.suntrans.tenement.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class MyAxisValueFormatter implements IAxisValueFormatter
{


    private List<String> xRealValues;

    public MyAxisValueFormatter(List<String> xRealValues) {
        this.xRealValues = xRealValues;
//        System.out.println(xRealValues);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        System.out.println("value before formatted is:"+value);
//        System.out.println("value after formatted is:"+xRealValues.get((int) value));
//        XAxis axis1 = (XAxis) axis;
//        axis1.getPosition().
        int index = (int) value;
        if (index <= xRealValues.size()-1){
            return xRealValues.get(index);
        }
        return "";
    }

}
