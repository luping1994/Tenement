package net.suntrans.tenement.ui.activity.admin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.SimpleData;
import net.suntrans.tenement.databinding.ActivityPaymentBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.activity.rent.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */
public class PaymentActivity_wuye extends BasedActivity {

    private ActivityPaymentBinding binding;

    private List<SimpleData> datas;
    private String[] funName;
    private Handler handler = new Handler();
    // 图片封装为一个数组
    private int[] icon = {R.drawable.ic_dianfei,
            R.drawable.ic_wuyefei, R.drawable.ic_zujin};

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
                        Intent intent = new Intent(PaymentActivity_wuye.this, EleChargeActivity_admin.class);
                        intent.putExtra("title","电费");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(PaymentActivity_wuye.this, EleChargeActivity_admin.class);
                        intent2.putExtra("title","物业费");
                        startActivity(intent2);
                        break;
                    case 2:
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

     class SimpleAdapter extends BaseQuickAdapter<SimpleData, BaseViewHolder> {

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
