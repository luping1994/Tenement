package net.suntrans.tenement.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class ChannelEditActivity extends BasedActivity {

    private String channel_id;
    private String channel_type;
    private Spinner spinner;
    private EditText nameTx;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_editor);
        channel_id = getIntent().getIntExtra("id",0)+"";
        channel_type = getIntent().getIntExtra("channel_type",0)+"";
        spinner = (Spinner) findViewById(R.id.type);
        nameTx = (EditText) findViewById(R.id.name);
        name = getIntent().getStringExtra("title");
        nameTx.setText(name);
        nameTx.setSelection(name.length()>10?10:name.length());

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        if ("1".equals(channel_type)) {
            spinner.setSelection(0);
        } else if ("2".equals(channel_type)) {
            spinner.setSelection(1, true);

        } else {
            spinner.setSelection(0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        if (channel_id == null)
            return;
    }

    boolean committing = false;

    private void upDate(String id, String name, String channel_type) {

        if (committing) {
            UiUtils.INSTANCE.showToast("正在修改请稍后...");
            return;
        }
//        System.out.println(id+","+name+","+channel_type);
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("device_type", channel_type);
        committing = true;
        addSubscription(RetrofitHelper.getApi().updateChannel(map), new BaseSubscriber<ResultBody>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                committing = false;
            }

            @Override
            public void onNext(ResultBody result) {
                committing = false;
                UiUtils.INSTANCE.showToast(result.msg);
            }
        });
    }

    public void commit(View view) {
        String name = nameTx.getText().toString();
        String type = (spinner.getSelectedItemPosition() + 1) + "";
        if (TextUtils.isEmpty(name)) {
            UiUtils.INSTANCE.showToast("名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(type)) {
            UiUtils.INSTANCE.showToast("类型不能为空");
            return;
        }
        upDate(channel_id,name,type);
    }
}
