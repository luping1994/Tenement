package net.suntrans.tenement.ui.activity.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityAddCompanyBinding;
import net.suntrans.tenement.databinding.ActivityAddStuffBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class AddCompanyActivity extends BasedActivity implements View.OnClickListener {

    private ActivityAddCompanyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_company);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings settings = binding.webview.getSettings();

        settings.setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        binding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });

        binding.webview.loadUrl("file:///android_asset/html/add_company.html");
    }


    @Override
    public void onClick(View view) {


    }

    protected void addStuff(String username, String password, String name, String telephone) {

//        addSubscription(api.addStuff(map), new BaseSubscriber<ResultBody>(this) {
//            @Override
//            public void onNext(ResultBody resultBody) {
//                UiUtils.INSTANCE.showToast(resultBody.msg);
//            }
//        });
    }
}
