package net.suntrans.tenement.ui.activity.rent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityAddCompanyBinding;
import net.suntrans.tenement.databinding.ActivityRepairBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class RepairActivity extends BasedActivity implements View.OnClickListener {

    private ActivityRepairBinding binding;

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

        binding.webview.loadUrl("file:///android_asset/html/repair.html");
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
