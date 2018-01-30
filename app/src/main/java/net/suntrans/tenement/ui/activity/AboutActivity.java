package net.suntrans.tenement.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.suntrans.tenement.BuildConfig;
import net.suntrans.tenement.R;
/**
 * Created by Looney on 2017/7/24.
 */

public class AboutActivity extends BasedActivity {
    private TextView guangwang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView =findViewById(R.id.version);
        textView.setText(getString(R.string.tx_version_code) + BuildConfig.VERSION_NAME);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(getString(R.string.tx_share_app));
            }
        });

        findViewById(R.id.guangwang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //从其他浏览器打开
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse("http://www.suntrans.net");

                intent.setData(content_url);
                startActivity(Intent.createChooser(intent, "请选择浏览器"));

            }
        });

        findViewById(R.id.rexian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"020-86361851"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void share(String desc) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.tx_share_app_des));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, desc));
    }


}
