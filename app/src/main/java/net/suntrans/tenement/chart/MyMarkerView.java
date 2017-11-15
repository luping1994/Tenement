
package net.suntrans.tenement.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


import net.suntrans.tenement.R;

import java.text.SimpleDateFormat;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SimpleDateFormat mFormat = new SimpleDateFormat("MM月dd日HH:mm");//created_at=2017-10-23 15:30:29
        String format = "";
        try {
            format =  mFormat.format(e.getX());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(format+"  " + ChartUtils.formatNumber(ce.getHigh(), 2, true));
        } else {

            tvContent.setText(format+"  "+ ChartUtils.formatNumber(e.getY(), 2, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
