package net.suntrans.tenement.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.supl.ISlidingUpPanel;
import com.xw.repo.supl.SlidingUpPanelLayout;

import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.WeatherModel;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-07-10.
 */

public class WeatherPanelView extends BaseWeatherPanelView implements View.OnClickListener {

    private View mContentLayout;
    private View mMenuLayout;
    private View mExpendLayout;
    private ImageView mCollapseImg;
    private ImageView mSettingsImg;
    private TextView mCityText;
    private ImageView mWeatherIcon;
    private TextView mWeatherDescText;
    private TextView mTempNowText;
    private TextView mAqiDescText;
    private ImageView mDay1WeatherIcon;
    private TextView mDay1WeatherDescText;
    private TextView mDay1TempRangeText;
    private ImageView mDay2WeatherIcon;
    private TextView mDay2WeatherDescText;
    private TextView mDay2TempRangeText;
    private ImageView mDay3WeatherIcon;
    private TextView mDay3WeatherDescText;
    private TextView mDay3TempRangeText;

    private View mCollapseLayout;
    private TextView mCityTextCollapse;
    private TextView mWeatherDescTextCollapse;
    private TextView mTempNowTextCollapse;
    private ImageView mWeatherIconCollapse;

    private int mWeatherTypeCode;

    public WeatherPanelView(Context context) {
        this(context, null);
    }

    public WeatherPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.content_weather_panel_view, this, true);
        mContentLayout = findViewById(R.id.panel_content_layout);
        mMenuLayout = findViewById(R.id.panel_menu_layout);
        mExpendLayout = findViewById(R.id.panel_expend_layout);
        mCollapseImg = (ImageView) findViewById(R.id.panel_collapse_img);
        mSettingsImg = (ImageView) findViewById(R.id.panel_settings_img);
        mCityText = (TextView) findViewById(R.id.panel_city_text);
        mWeatherIcon = (ImageView) findViewById(R.id.panel_weather_icon);
        mWeatherDescText = (TextView) findViewById(R.id.panel_weather_desc_text);
        mTempNowText = (TextView) findViewById(R.id.panel_temp_now_text);
        mAqiDescText = (TextView) findViewById(R.id.panel_air_condition_text);
        mDay1WeatherIcon = (ImageView) findViewById(R.id.day1_weather_icon);
        mDay1WeatherDescText = (TextView) findViewById(R.id.day1_weather_desc_text);
        mDay1TempRangeText = (TextView) findViewById(R.id.day1_temp_range_text);
        mDay2WeatherIcon = (ImageView) findViewById(R.id.day2_weather_icon);
        mDay2WeatherDescText = (TextView) findViewById(R.id.day2_weather_desc_text);
        mDay2TempRangeText = (TextView) findViewById(R.id.day2_temp_range_text);
        mDay3WeatherIcon = (ImageView) findViewById(R.id.day3_weather_icon);
        mDay3WeatherDescText = (TextView) findViewById(R.id.day3_weather_desc_text);
        mDay3TempRangeText = (TextView) findViewById(R.id.day3_temp_range_text);
        mCollapseLayout = findViewById(R.id.panel_collapse_layout);
        mCityTextCollapse = (TextView) findViewById(R.id.panel_city_text_collapse);
        mWeatherDescTextCollapse = (TextView) findViewById(R.id.panel_weather_desc_text_collapse);
        mTempNowTextCollapse = (TextView) findViewById(R.id.panel_temp_now_collapse);
        mWeatherIconCollapse = (ImageView) findViewById(R.id.panel_weather_icon_collapse);
        mCollapseImg.setOnClickListener(this);
        mSettingsImg.setOnClickListener(this);

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);
        mMenuLayout.setPadding(0, statusBarHeight, 0, 0);
        mExpendLayout.setPadding(0, statusBarHeight, 0, 0);

        checkVisibilityOfViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.panel_collapse_img:
                if (mCollapseImg.getAlpha() == 1) {
                    ((SlidingUpPanelLayout) getParent()).collapsePanel();
                }

                break;
            case R.id.panel_settings_img:
                if (mSettingsImg.getAlpha() >= 1) {
                    Toast.makeText(getContext(), "settings", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void setSlideState(@SlidingUpPanelLayout.SlideState int slideState) {
        super.setSlideState(slideState);

        checkVisibilityOfViews();
    }

    @Override
    public void onSliding(@NonNull ISlidingUpPanel panel, int top, int dy, float slidedProgress) {
        super.onSliding(panel, top, dy, slidedProgress);

        if (dy < 0) { // 向上
            float radius = getRadius();
            if (radius > 0 && MAX_RADIUS >= top) {
                setRadius(top);
            }

            float alpha = mCollapseLayout.getAlpha();
            if (alpha > 0f && top < 200) {
                alpha += dy / 200.0f;
                mCollapseLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }

            alpha = mMenuLayout.getAlpha();
            if (alpha < 1f && top < 100) {
                alpha -= dy / 100.0f;
                mMenuLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }

            alpha = mExpendLayout.getAlpha();
            if (alpha < 1f) {
                alpha -= dy / 1000.0f;
                mExpendLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }
        } else { // 向下
            float radius = getRadius();
            if (radius < MAX_RADIUS) {
                radius += top;
                setRadius(radius > MAX_RADIUS ? MAX_RADIUS : radius);
            }

            float alpha = mCollapseLayout.getAlpha();
            if (alpha < 1f) {
                alpha += dy / 800.0f;
                mCollapseLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }

            alpha = mMenuLayout.getAlpha();
            if (alpha > 0f) {
                alpha -= dy / 100.0f;
                mMenuLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }

            alpha = mExpendLayout.getAlpha();
            if (alpha > 0f) {
                alpha -= dy / 1000.0f;
                mExpendLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }
        }
    }

    @Override
    public void setWeatherModel(WeatherModel weather) {
        mWeather = weather;
        if (weather == null)
            return;


        checkVisibilityOfViews();
    }

    private void checkVisibilityOfViews() {
        if (mWeatherTypeCode == 1) {
            mContentLayout.setBackgroundColor(Color.parseColor("#80DEEA"));
        } else if (mWeatherTypeCode == 2) {
            mContentLayout.setBackgroundColor(Color.parseColor("#78909C"));
        } else {
            mContentLayout.setBackgroundColor(Color.parseColor("#03A9F4"));
        }

        if (mSlideState == SlidingUpPanelLayout.COLLAPSED) {
            setRadius(MAX_RADIUS);

            mMenuLayout.setAlpha(0f);
            mExpendLayout.setAlpha(0f);
            mCollapseLayout.setAlpha(1f);
        } else if (mSlideState == SlidingUpPanelLayout.EXPANDED) {
            setRadius(0);

            mMenuLayout.setAlpha(1f);
            mExpendLayout.setAlpha(1f);
            mCollapseLayout.setAlpha(0f);
        }
    }
}
