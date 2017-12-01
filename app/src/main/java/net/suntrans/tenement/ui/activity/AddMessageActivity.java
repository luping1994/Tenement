package net.suntrans.tenement.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import net.suntrans.common.utils.LogUtil;
import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.MainActivity;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.Image;
import net.suntrans.tenement.bean.ImgItem;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityAddMessageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class AddMessageActivity extends BasedActivity {

    private ActivityAddMessageBinding binding;
    private AddImageAdapter adapter;

    private static final int REQUEST_CODE_CHOOSE = 101;
    private List<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_message);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();

    }

    private void init() {
        View footer = LayoutInflater.from(this.getApplicationContext())
                .inflate(R.layout.footer_addimg, binding.recyclerView, false);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelected.size() >= 3) {
                    UiUtils.INSTANCE.showToast("最多只能上传3张图片");
                    return;
                }
                Matisse.from(AddMessageActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(true)
                        .capture(true)
                        .maxSelectable(3)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(1)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
        adapter = new AddImageAdapter(R.layout.item_add_image, mSelected);
        adapter.addFooterView(footer, 0, LinearLayout.HORIZONTAL);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    mSelected.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    List<Uri> mSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (mSelected.size() >= 3) {
                UiUtils.INSTANCE.showToast("最多只能上传3张图片");
                return;
            }
            mSelected.addAll(Matisse.obtainResult(data));
            paths = Matisse.obtainPathResult(data);
            LogUtil.INSTANCE.i("Matisse", "mSelected: " + mSelected);
            adapter.notifyDataSetChanged();

        }
    }

    private LoadingDialog dialog;

    public void rightSubTitleClicked(View view) {
//        api.updateStuffProfile()
//        if (paths.size()!=0){
//
//            if (dialog == null) {
//                dialog = new LoadingDialog(this);
//                dialog.setCancelable(false);
//                dialog.setWaitText("请稍后..");
//            }
//            dialog.show();
//
//            dialog.setWaitText(getString(R.string.tips_uploading));
//            //添加多张图片
//            MultipartBody.Builder builder = new MultipartBody.Builder();
//            builder.setType(MultipartBody.FORM);
//            for (int i=0;i<paths.size();i++){
//                File file = new File(paths.get(i));
//                MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName());
//                builder.addPart(imageBodyPart);
//
//            }
//        }
    }


    private class AddImageAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

        public AddImageAdapter(int layoutResId, @Nullable List<Uri> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Uri item) {
            helper.addOnClickListener(R.id.delete);
            ImageView imageView = helper.getView(R.id.image);
            Glide.with(AddMessageActivity.this)
                    .load(item)
                    .centerCrop()
                    .into(imageView);
        }
    }

    private void storeNotice(String title, String contents, String vtype, String images) {
        if (TextUtils.isEmpty(title)) {
            UiUtils.INSTANCE.showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(contents)) {
            UiUtils.INSTANCE.showToast("请输入内容");
            return;
        }
        if (TextUtils.isEmpty(vtype)) {
            UiUtils.INSTANCE.showToast("请选择类型");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("contents", contents);
        map.put("vtype", vtype);
        if (!TextUtils.isEmpty(images))
            map.put("images", images);

        addSubscription(api.storeNotice(map), new BaseSubscriber<ResultBody>() {
            @Override
            public void onNext(ResultBody body) {
                UiUtils.INSTANCE.showToast(body.msg);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void startUpload(){
        CountDownLatch latch  = new CountDownLatch(3);
    }


}
