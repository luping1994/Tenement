package net.suntrans.tenement.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityFeedbackBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/9/14.
 */

public class FeedbackActivity extends BasedActivity implements View.OnClickListener {

    private int currentSelected;
    private ActivityFeedbackBinding binding;
    private int mCheckedTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        binding.commit.setOnClickListener(this);

        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mCheckedTypeId = checkedId;
            }
        });
        binding.jianyi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.count.setText(String.format(getString(R.string.count),s.length()));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.commit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadingDialog dialog;

    @Override
    public void onClick(View v) {

    }

    private void commitSuggestion() {
        String type = "";
        if (mCheckedTypeId == R.id.chanpinjianyi) {
            type = "1";
        } else if (mCheckedTypeId == R.id.chengxucuowu) {
            type = "2";
        } else {
            UiUtils.showToast(getString(R.string.title_feedback_type));
            return;
        }
        String qus = binding.jianyi.getText().toString();
        String email = binding.email.getText().toString();
        if (TextUtils.isEmpty(qus)) {
            UiUtils.showToast(getString(R.string.tips_content_is_empty));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            UiUtils.showToast(getString(R.string.tips_contacts_is_empty));
            return;
        }
        if (!email.matches("^[_a-zA-Z0-9\\-\\.]+@([\\-_a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}$") && !email.matches("^[1-9]\\d{4,12}$")) {
            UiUtils.showToast(getString(R.string.tips_contacts_is_error));
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText(getString(R.string.tips_please_wait));
        }
        dialog.show();

        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("contents",qus);
        map.put("link",email);
        RetrofitHelper.getApi().postSuggestion(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<ResultBody>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        dialog.dismiss();


                    }

                    @Override
                    public void onNext(ResultBody resultBody) {
                        dialog.dismiss();
                        new AlertDialog.Builder(FeedbackActivity.this)
                                .setCancelable(false)
                                .setMessage(R.string.tips_feedbak_success)
                                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create().show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void rightSubTitleClicked(View view) {
        commitSuggestion();

    }
}
