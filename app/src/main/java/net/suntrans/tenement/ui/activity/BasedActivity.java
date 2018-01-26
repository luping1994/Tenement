package net.suntrans.tenement.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import net.suntrans.common.utils.StatusBarCompat;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class BasedActivity extends AppCompatActivity {
    public final static List<BasedActivity> mlist = new LinkedList<>();


    protected Api api = RetrofitHelper.getApi();
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();


    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        synchronized (mlist) {
            mlist.add(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fixStatusBar();
    }

    @Override
    protected void onDestroy() {
        synchronized (mlist) {
            mlist.remove(this);
        }
        onUnsubscribe();
        super.onDestroy();
    }

    public void killAll() {
        List<BasedActivity> copy;
        synchronized (mlist) {
            copy = new LinkedList<BasedActivity>(mlist);
            for (BasedActivity a :
                    copy) {
                a.finish();
            }
        }
    }

    private void fixStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View statusBarFix = findViewById(R.id.statusBarFix);
            int statusBarHeight = StatusBarCompat.getStatusBarHeight(this.getApplicationContext());
            if (statusBarFix != null)
                statusBarFix.getLayoutParams().height = statusBarHeight;
        }
    }
}
