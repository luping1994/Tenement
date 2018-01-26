package net.suntrans.tenement.ui.activity.rent;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xw.repo.supl.ISlidingUpPanel;
import com.xw.repo.supl.SlidingUpPanelLayout;

import net.suntrans.common.utils.StatusBarCompat;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.looney.widgets.cardstack.RxAdapterStack;
import net.suntrans.looney.widgets.cardstack.RxCardStackView;
import net.suntrans.tenement.Constants;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.PayOrder;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityElePayBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.xw.repo.supl.SlidingUpPanelLayout.COLLAPSED;
import static com.xw.repo.supl.SlidingUpPanelLayout.HIDDEN;

/**
 * Created by Looney on 2017/12/5.
 * Des:租户电费缴费页面
 */

public class EleChargeActivity extends BasedActivity implements RxCardStackView.ItemExpendListener {

    private ActivityElePayBinding binding;
    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ele_pay);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        //以下临时用
        String source = getIntent().getStringExtra("source");


        initView();
    }

    private void initView() {



//        mBgLayout.setPadding(0, StatusBarCompat.getStatusBarHeight(this), 0, 0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void getData(){

        addSubscription(RetrofitHelper.getApi().getPayRoom(),new BaseSubscriber<ResultBody<List<Map<String, String>>>>(){
            @Override
            public void onNext(ResultBody<List<Map<String, String>>> mapResultBody) {
                super.onNext(mapResultBody);
                String id = mapResultBody.data.get(0).get("id");

            }
        });


    }

    @Override
    public void onItemExpend(boolean expend) {

    }

    public void payByWX(View view) {
//        RetrofitHelper.getApi().getWXOrder("2","0.01","android","1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResultBody<PayOrder>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(ResultBody<PayOrder> result) {
//                        if (wxapi == null) {
//                            wxapi = WXAPIFactory.createWXAPI(EleChargeActivity.this, Constants.APP_ID);
//                        }
//                        System.out.println(result.data.toString());
//                        wxapi.registerApp(Constants.APP_ID);
//                        PayReq req = new PayReq();
//
//                        req.appId = result.data.appid;  // 测试用appId
//                        req.partnerId = result.data.partnerid;
//                        req.prepayId = result.data.prepayid;
//                        req.nonceStr = result.data.noncestr;
//                        req.timeStamp = result.data.timestamp;
//                        req.packageValue = result.data.packageX;
//                        req.sign = result.data.newsign;
//
//                        wxapi.sendReq(req);
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        wxapi.detach();

        super.onDestroy();
    }

}
