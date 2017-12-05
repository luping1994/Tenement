package net.suntrans.tenement.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.EnergyHis;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.chart.DayAxisValueFormatter;
import net.suntrans.tenement.chart.MyAxisValueFormatter2;
import net.suntrans.tenement.chart.XYMarkerView;
import net.suntrans.tenement.databinding.ActivityEnergyConsumeBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */
public class EnergyConsumeActivity extends BasedActivity {

    private ActivityEnergyConsumeBinding binding;

    private Handler handler = new Handler();
    private int lastDay;
    private String id;
    private String sno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_energy_consume);
        lastDay = getCurrentMonthLastDay();
        id = getIntent().getStringExtra("id");
//        binding.todayUsedValue.setText(getIntent().getStringExtra("todyUsed")+"kW·h");
//        binding.yesterdayUsedValue.setText(getIntent().getStringExtra("yesterdayUsed")+"kW·h");
//        binding.monthUsed.setText(getIntent().getStringExtra("monthUsed")+"");

        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(sno)){
                    UiUtils.showToast("无法获取电表信息");
                    return;
                }
                Intent intent = new Intent(EnergyConsumeActivity.this, AmmeterParameterActivity.class);
                intent.putExtra("sno",sno);
                startActivity(intent);
            }
        });
        initView();

        initYearChart();
        initMonthChart();

    }

    private void initView() {
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            binding.title.setText(title);
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
//                        setMonthData();
                        getEnergyDetailByID(id);
                    }
                }, 900);
            }
        });


    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void initYearChart() {

        binding.yearEle.setDrawBarShadow(false);
        binding.yearEle.setDrawValueAboveBar(true);
        binding.yearEle.getAxisRight().setEnabled(false);
        binding.yearEle.setTouchEnabled(true);
        binding.yearEle.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.yearEle.setMaxVisibleValueCount(60);
//        binding.yearEle.setOnChartValueSelectedListener(this);

        // scaling can now only be done on x- and y-axis separately
        binding.yearEle.setPinchZoom(true);
        binding.yearEle.setDrawGridBackground(false);
        // binding.yearEle.setDrawYLabels(false);


        XAxis xAxis = binding.yearEle.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12);
        xAxis.setGranularity(1f); // only intervals of 1 day

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "月";
            }
        });

        IAxisValueFormatter custom = new MyAxisValueFormatter2();

        YAxis leftAxis = binding.yearEle.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(4, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        Legend l = binding.yearEle.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(binding.yearEle, DayAxisValueFormatter.DAYS);
        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter,XYMarkerView.YEAR);
        mv.setChartView(binding.yearEle); // For bounds control
        binding.yearEle.setMarker(mv); // Set the marker to the chart
    }

    private void initMonthChart() {
        binding.monthEle.setDrawBarShadow(false);
        binding.monthEle.setDrawValueAboveBar(true);
        binding.monthEle.getAxisRight().setEnabled(false);
        binding.monthEle.setTouchEnabled(true);
        binding.monthEle.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.monthEle.setMaxVisibleValueCount(60);
//        binding.yearEle.setOnChartValueSelectedListener(this);

        // scaling can now only be done on x- and y-axis separately
        binding.monthEle.setPinchZoom(true);
        binding.monthEle.setDrawGridBackground(false);
        // binding.yearEle.setDrawYLabels(false);


        XAxis xAxis = binding.monthEle.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        };
        xAxis.setValueFormatter(iAxisValueFormatter);


        YAxis leftAxis = binding.monthEle.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(4, true);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setSpaceMin(1);
        leftAxis.setGridLineWidth(1);


        Legend l = binding.monthEle.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        XYMarkerView mv = new XYMarkerView(this, iAxisValueFormatter,XYMarkerView.MONTH);
        mv.setChartView(binding.monthEle); // For bounds control
        binding.monthEle.setMarker(mv); // Set the marker to the chart
    }

    private void setMonthData(List<EnergyHis.HisItem> monthData,String month) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        if (monthData == null || monthData.size() == 0)
            return;

        for (int i = 1; i <=lastDay; i++) {
            float val = 0;
            for (int j = 0; j < monthData.size(); j++) {
                if (monthData.get(j).x == i) {
                    val = monthData.get(j).y;
                }
            }

            BarEntry entry = new BarEntry(i, val);
            yVals1.add(entry);
        }
        BarDataSet set1;
        if (binding.monthEle.getData() != null &&
                binding.monthEle.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.monthEle.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            binding.monthEle.getData().notifyDataChanged();
            binding.monthEle.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "本月("+month+"月)用电量");
            set1.setColors(MONTH_BAR_COLOR);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.8f);

            binding.monthEle.setData(data);
        }
        binding.monthEle.invalidate();
        binding.monthEle.animateY(800);

    }

    private void setYearData(List<EnergyHis.HisItem> yearData,String year) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        for (int i = 1; i <=12; i++) {
            float val = 0;
            for (int j = 0; j < yearData.size(); j++) {
                if (yearData.get(j).x == i) {
                    val = yearData.get(j).y;
                }
            }

            BarEntry entry = new BarEntry(i, val);
            yVals1.add(entry);
        }
        BarDataSet set1;
        if (binding.yearEle.getData() != null &&
                binding.yearEle.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.yearEle.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            binding.yearEle.getData().notifyDataChanged();
            binding.yearEle.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, year+"年每月用电量");
            set1.setColors(MONTH_BAR_COLOR);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setBarWidth(1.5f);

            binding.yearEle.setData(data);
        }
        binding.yearEle.invalidate();
        binding.yearEle.animateY(800);

    }

    public static final int[] MONTH_BAR_COLOR = {
            rgb("#c03530")
    };

    @Override
    protected void onResume() {
        super.onResume();
        getEnergyDetailByID(id);
    }

    private void getEnergyDetailByID(String id) {
        mCompositeSubscription.add(api.getEnergyDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<EnergyHis>>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultBody<EnergyHis> data) {
                        sno = data.data.sno;
                        binding.yesterdayUsedValue.setText(data.data.el_yesterday+"kW·h");
                        binding.todayUsedValue.setText(data.data.el_today+"kW·h");
                        binding.monthUsed.setText(data.data.el_month);
                        List<EnergyHis.HisItem> monthData = data.data.month;
                        List<EnergyHis.HisItem> yearData = data.data.year;
                        setMonthData(monthData,data.data.date_m);
                        setYearData(yearData,data.data.date_y);
                    }
                }));
    }

    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
