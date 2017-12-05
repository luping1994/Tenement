package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;

import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityRepairBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class MessageDetailActivity extends BasedActivity implements View.OnClickListener {

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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.title.setText("公告详情");
//        token = App.Companion.getMySharedPreferences().getString("token", "0");
//        house_id = "1";
        settings.setJavaScriptEnabled(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
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

        binding.webview.loadUrl(getIntent().getStringExtra("url"));
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
           new AlertDialog.Builder(MessageDetailActivity.this)
                   .setMessage("是否打开"+control.split(",")[1])
                   .setPositiveButton("确定",null)
                   .setNegativeButton("取消",null).create().show();


        }
    }

    protected void addStuff(String username, String password, String name, String telephone) {

//        addSubscription(api.addStuff(map), new BaseSubscriber<ResultBody>(this) {
//            @Override
//            public void onNext(ResultBody resultBody) {
//                UiUtils.showToast(resultBody.msg);
//            }
//        });
    }
}
