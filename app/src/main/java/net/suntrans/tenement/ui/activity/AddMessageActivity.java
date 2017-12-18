package net.suntrans.tenement.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import net.suntrans.common.utils.FileUtils;
import net.suntrans.common.utils.LogUtil;
import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.App;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.ActivityAddMessageBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.activity.rent.MessageActivity;
import net.suntrans.tenement.widgets.FullyGridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.suntrans.tenement.Constant.AUTHORITY;

import android.content.Context;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class AddMessageActivity extends BasedActivity {

    private ActivityAddMessageBinding binding;
    private AddImageAdapter adapter;

    private static final int REQUEST_CODE_CHOOSE = 101;
    private List<String> paths = new CopyOnWriteArrayList<>();

    private String type = "";
    private String title;
    private String content;

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

    private int maxImgSize = 5;

    private void init() {

        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new AddImageAdapter(mSelected, this);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddImageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == -1)
                    return;
                if (position == mSelected.size()) {
                    if (mSelected.size() >= maxImgSize) {
                        UiUtils.showToast("最多只能上传5张图片");
                        return;
                    }
                    Matisse.from(AddMessageActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                            .countable(true)
                            .capture(true)
                            .captureStrategy(new CaptureStrategy(true, AUTHORITY))
                            .maxSelectable(maxImgSize - mSelected.size())
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(1)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                } else {
                    mSelected.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    type = "1";
                } else if (checkedId == R.id.radio1) {
                    type = "2";
                } else if (checkedId == R.id.radio2) {
                    type = "3";
                }
            }
        });

    }

    List<Uri> mSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (mSelected.size() >= maxImgSize) {
                UiUtils.showToast("最多只能上传5张图片");
                return;
            }
            mSelected.addAll(Matisse.obtainResult(data));
            paths.addAll(Matisse.obtainPathResult(data));
            LogUtil.INSTANCE.i("Matisse", "mSelected: " + mSelected);
            LogUtil.INSTANCE.i("Matisse", "paths: " + paths);
            adapter.notifyDataSetChanged();

        }
    }

    private LoadingDialog dialog;

    public void rightSubTitleClicked(View view) {
//        upload();
        title = binding.title.getText().toString();
        content = binding.content.getText().toString();

        if (TextUtils.isEmpty(title)) {
            UiUtils.showToast("标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            UiUtils.showToast("内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(type)) {
            UiUtils.showToast("请选择类型");
            return;
        }


        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setCancelable(true);
            dialog.setWaitText("发布中,请稍后...");
        }
        dialog.show();
        if (mSelected.size() == 0) {
            storeNotice(title, content, type, "");
            return;
        }
        upload();

    }


    private static class AddImageAdapter extends RecyclerView.Adapter {

        private final List<Uri> datas;
        private final Context context;
        public static final int imgSize = UiUtils.dip2px(72);

        public AddImageAdapter(@Nullable List<Uri> data, Context context) {
            this.datas = data;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return new ViewHolder1(LayoutInflater.from(context).inflate(R.layout.item_add_image, parent, false));
            }
            return new ViewHolder2(LayoutInflater.from(context).inflate(R.layout.footer_addimg, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == datas.size())
                return 1;
            return 0;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder {

            RelativeLayout delete;
            ImageView imageView;

            public ViewHolder1(View itemView) {
                super(itemView);
                delete = itemView.findViewById(R.id.delete);
                imageView = itemView.findViewById(R.id.image);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(getAdapterPosition());
                        }
                    }
                });
            }

            public void setData(int position) {
                Glide.with(context)
                        .load(datas.get(position))
                        .override(imgSize, imgSize)
                        .centerCrop()
                        .into(imageView);
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder {


            public ViewHolder2(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(getAdapterPosition());
                        }
                    }
                });
            }

            public void setData(int position) {

            }
        }

        private onItemClickListener onItemClickListener;

        public void setOnItemClickListener(AddImageAdapter.onItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface onItemClickListener {
            void onItemClick(int position);
        }
    }

    private void storeNotice(String title, String contents, String vtype, String images) {

        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("contents", contents);
        map.put("vtype", vtype);
        if (!TextUtils.isEmpty(images))
            map.put("images", images);


        binding.rightSubTitle.setClickable(false);
        addSubscription(api.storeNotice(map), new BaseSubscriber<ResultBody>() {
            @Override
            public void onNext(ResultBody body) {
                UiUtils.showToast(body.msg);
                if (dialog != null) {
                    dialog.dismiss();
                }
                binding.rightSubTitle.setClickable(true);

                new IosAlertDialog(AddMessageActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setTitle("发布成功!")
                        .setPositiveButton("去查看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AddMessageActivity.this, MessageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (dialog != null) {
                    dialog.dismiss();
                }
                binding.rightSubTitle.setClickable(true);


            }
        });
    }

    private List<String> uploadedImages = new CopyOnWriteArrayList<>();


    //先定义一个请求接口，除了图片可能还有其他一些参数需要上传，所以还定义了个map。接下来开始正文：
    public void upload() {
        mCompositeSubscription.add(Observable.from(paths)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String path) {
                        //压缩图片并且返回压缩后的图片文件
                        File cacheDir = App.Companion.getApplication().getCacheDir();
                        String tempPath = cacheDir.getAbsolutePath() + File.separator + System.currentTimeMillis();
                        LogUtil.INSTANCE.i(tempPath);
                        File file = FileUtils.compressImage(path, tempPath, 80);
                        return file;
                    }
                })
                .map(new Func1<File, MultipartBody.Part>() {
                    @Override
                    public MultipartBody.Part call(File file) {
                        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
                        return imageBodyPart;
                    }
                })
                .flatMap(new Func1<MultipartBody.Part, Observable<ResultBody<Map<String, String>>>>() {
                    @Override
                    public Observable<ResultBody<Map<String, String>>> call(MultipartBody.Part part) {
                        return api.uploadNoticeFile(part);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<ResultBody<Map<String, String>>>() {
                    @Override
                    public void onCompleted() {

//                        System.out.println("完成");
                        LogUtil.INSTANCE.i("上传任务完成");
                        StringBuilder builder = new StringBuilder();
                        for (String s :
                                uploadedImages) {
                            builder.append(s)
                                    .append(",");
                        }
                        String images = builder.toString();
                        images = images.substring(0, images.length() - 1);
                        storeNotice(title, content, type, images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ResultBody<Map<String, String>> result) {
                        String image = result.data.get("image");
                        LogUtil.INSTANCE.i(image);
                        uploadedImages.add(image);
                    }
                }));
    }

}
