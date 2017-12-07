package net.suntrans.tenement.ui.activity.rent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ProfileWraper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SimpleData;
import net.suntrans.tenement.databinding.ActivityPaymentBinding;
import net.suntrans.tenement.persistence.AppDatabase;
import net.suntrans.tenement.persistence.User;
import net.suntrans.tenement.persistence.UserDao;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.activity.ProfileActivity;
import net.suntrans.tenement.ui.activity.admin.EleChargeActivity_admin;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */
public class PaymentActivity extends BasedActivity {
    private ActivityPaymentBinding binding;

    private List<SimpleData> datas;
    private String[] funName;
    private Handler handler = new Handler();
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_dianfei,
            R.drawable.ic_wuyefei, R.drawable.ic_zujin};
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        initView();
    }

    private void initView() {
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            binding.title.setText(title);
        }
        if (UiUtils.isNetworkAvailable()) {
            getUserProfile();
        } else {
            getDataFromLocal();
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                }, 900);
            }
        });

        funName = new String[]{"电费", "物业费", "租金"};
        SimpleAdapter adapter = new SimpleAdapter(R.layout.item_home_page_fun_admin, getData());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(PaymentActivity.this,EleChargeActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(PaymentActivity.this,WuyeChargeActivity.class));
                        break;
                    case 2:
                        UiUtils.showToast("该功能如需开通请咨询管理员");
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public void rightSubTitleClicked(View view) {

    }

    private void getDataFromLocal() {
        final int id = App.Companion.getMySharedPreferences().getInt("id", 0);
        mCompositeSubscription.add(Observable.just(AppDatabase.getInstance(this)
                .userDao())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<UserDao, Observable<User>>() {
                    @Override
                    public Observable<User> call(UserDao userDao) {
                        User userById = userDao.getUserById(id);
                        return Observable.just(userById);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User info) {
                        user = info;
                        binding.name.setText(user.company_name);
                        Glide.with(PaymentActivity.this)
                                .load(user.cover)
                                .asBitmap()
                                .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        binding.image.setImageBitmap(resource);
                                    }
                                });

                    }
                }));
    }

    private void getUserProfile() {
        api.loadUserInfo()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<ResultBody<ProfileWraper>>() {
                    @Override
                    public void call(ResultBody<ProfileWraper> profileWraperResultBody) {
                        AppDatabase.getInstance(PaymentActivity.this)
                                .userDao().updateUser(profileWraperResultBody.data.user);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<ProfileWraper>>(this) {
                    @Override
                    public void onNext(ResultBody<ProfileWraper> result) {


                        user = result.data.user;
                        binding.name.setText(user.company_name);

                        Glide.with(PaymentActivity.this)
                                .load(user.cover)
                                .asBitmap()
                                .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        binding.image.setImageBitmap(resource);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }

    static class SimpleAdapter extends BaseQuickAdapter<SimpleData, BaseViewHolder> {

        public SimpleAdapter(int layoutResId, @Nullable List<SimpleData> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SimpleData item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.imgResId);
        }
    }

    public List<SimpleData> getData() {
        datas = new ArrayList<>();
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            SimpleData data = new SimpleData(icon[i], funName[i]);

            datas.add(data);
        }
        return datas;
    }


}
