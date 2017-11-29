
package net.suntrans.tenement.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


import net.suntrans.tenement.R;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;
    public static final int YEAR = 1;
    public static final int MONTH = 2;

    private int type = 1;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, int type) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("####");
        this.type = type;
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (type == YEAR)
            tvContent.setText((int) e.getX() + "月:" + e.getY() + "kW·h");
        else if (type == MONTH)
            tvContent.setText((int)e.getX() + "日:" + e.getY() + "kW·h");

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
