package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.suntrans.tenement.App;
import net.suntrans.tenement.MainActivity;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityAddCompanyBinding;
import net.suntrans.tenement.databinding.ActivityRepairBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class RepairActivity extends BasedActivity implements View.OnClickListener {

    private ActivityRepairBinding binding;
    private String tokenJS;
    private String wholeJS;
    private String token;
    private String house_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repair);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings settings = binding.webview.getSettings();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQxYmVmZWZmYzZmNTE4NThlNDU0M2VlYmYxMzFjMmVmMjQzZmY2MmJjM2EyYmNjMTg4ZjBhNmRkODg0YjgxM2JmMTVlMDY2MTg2MDY4ODdkIn0.eyJhdWQiOiIyIiwianRpIjoiNDFiZWZlZmZjNmY1MTg1OGU0NTQzZWViZjEzMWMyZWYyNDNmZjYyYmMzYTJiY2MxODhmMGE2ZGQ4ODRiODEzYmYxNWUwNjYxODYwNjg4N2QiLCJpYXQiOjE1MTE3NzM3NDYsIm5iZiI6MTUxMTc3Mzc0NiwiZXhwIjoxNTEyMjkyMTQ2LCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.SxqL9OxAItrPJaHTaT0Lq3kmFr3Hwlf-qGmZyYs1ACGzZB0O9opKumZubwKG1Wdb3Njw6NxjxVPCpNQpv9RETosOl6sSug0AaFNI0w4Mo-3Yvv5AQwOmETILo2BNa9zg9mAIXsh3dcMww0EZWjDRZk7BFkGZUtvF3E8edAkqldsRY7EEaGYJy_TCaaMmplXi8SDA4fnfACJGNu9Z-mqsb1cNIThdbTczu1JQ-bHXWPmNEyP77P26n5jXDi8uHvZPSVOq4ZPcq_f8uhf1zZtmrDwvZF3rQoq6f3qS01xfqbooUQ0qNZqZFLW_18wzs0eKVst_8AnVNCnJ2st7mCtk1WKOJG1y_56KX1nDWLvFjHjF_D9pke0e7rWa83AuiKOTt80jMcxUmrGvvsI3io1rZuJg0HZrb9Wl-bk2ltS52rRd3A1sCApvZZ4Fn1MC55qP3sa3HtjP5caZOwWjnR4hwimxWZQKyjtCiZPoNw07J9tG95hs78RanrkRX1s-RMoYrd5Abrn3GDG2x0vXJIyGnNFn8445K-rHp5zgzZwc27g8zaE3gCqtimji8Y6f3Slj5Esv6t1AqvjNj4qJBx2YzpXw26h0VqbiukzxMuRRl9v-OHtmA_MxHQKUC9-s4TuUtTy16VvVPeavUYDg-830V5heTUIJ8_TY-KBzah5Uko0";
        house_id = "1";
        settings.setJavaScriptEnabled(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//        Log.d("maomao", "densityDpi = " + mDensity);
//        if (mDensity == 240) {
//            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        } else if (mDensity == 160) {
//            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        } else if(mDensity == 120) {
//            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
//            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else if (mDensity == DisplayMetrics.DENSITY_TV){
//            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }else{
//            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        }
//
//
///**
// * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
// * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
// */
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        insertJs();
//        binding.webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                String js = "";
//                js += "var newscript = document.createElement(\"script\");";
//                js += "newscript.src=\"./js/designer.js\";";
//                js += "newscript.onload=function(){"
//                        + "init(\""
//                        + token + "\",\""
//                        + house_id + "\");};";
//                js += "document.body.appendChild(newscript);";
//
//                System.out.println(js);
//                binding.webview.loadUrl("javascript:" + js);
//            }
//        });
//        binding.webview.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//
//            }
//        });
//        binding.webview.setInitialScale(100);

        binding.webview.loadUrl("file:///android_asset/html/repair.html");
//        binding.webview.loadUrl("file:///android_asset/plan/floor_plan.html");
//        binding.webview.addJavascriptInterface(new AndroidtoJs(), "control");
    }


    @Override
    public void onClick(View view) {


    }


    private void insertJs() {


    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void switchChannel(String control) {
            String[] split = control.split(",");
            String status = split[2].equals("true") ? "关闭" : "打开";
            new AlertDialog.Builder(RepairActivity.this)
                    .setMessage("是否" + status + split[1])
                    .setPositiveButton("确定", null)
                    .setNegativeButton("取消", null).create().show();


        }
    }

    protected void addStuff(String username, String password, String name, String telephone) {

//        addSubscription(api.addStuff(map), new BaseSubscriber<ResultBody>(this) {
//            @Override
//            public void onNext(ResultBody resultBody) {
//                UiUtils.INSTANCE.showToast(resultBody.msg);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        binding.webview.destroy();
        super.onDestroy();
    }
}
