package net.suntrans.tenement.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;


import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.UploadInfo;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/8/16.
 */

public class UpLoadImageFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 0x02;
    public static final String SCALE_TYPE_16_9 = "16:9";
    public static final String SCALE_TYPE_1_1 = "1:1";
    private static final String CROPPED_IMAGE_NAME = "SceneCropImage.jpg";
    public String mCurrentPhotoPath;
    static String FILE_PROVIDER = "net.suntrans.tenement.fileProvider";
    private Uri photoURI;
    private ImageView ll;
    private Subscription subscribe;
    private String destinationFileName;
    private String type;

    public static UpLoadImageFragment newInstance(String type) {
        UpLoadImageFragment dialogFragment = new UpLoadImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.gallery).setOnClickListener(this);
        view.findViewById(R.id.takePhoto).setOnClickListener(this);
        ll =  view.findViewById(R.id.root);
        type = getArguments().getString("type");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery:
                openGallery();
                break;
            case R.id.takePhoto:
                dispatchTakePictureIntent(FILE_PROVIDER);
                break;
        }
    }

    private void openGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            UiUtils.INSTANCE.showToast("无法获取存储权限,该功能将不可用");
            new AlertDialog.Builder(getContext())
                    .setMessage("无法获取存储权限,是否前往设置")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", "net.suntrans.tenement", null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    private void dispatchTakePictureIntent(String fileprovider) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        fileprovider,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("requestCode =" + requestCode + ",resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                startCropActivity(photoURI);
            } else if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    UiUtils.INSTANCE.showToast("选择图片失败");
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else {
                UiUtils.INSTANCE.showToast("服务器错误");
            }
        } else {
            UiUtils.INSTANCE.showToast("取消操作");
        }

    }

    private void startCropActivity(@NonNull Uri uri) {
        destinationFileName = CROPPED_IMAGE_NAME;

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getContext().getCacheDir(), destinationFileName)));
        if (type.equals(SCALE_TYPE_16_9)) {
            uCrop.withAspectRatio(16, 9);
        } else if (type.equals(SCALE_TYPE_1_1)) {
            uCrop.withAspectRatio(1, 1);
            uCrop.withMaxResultSize(UiUtils.INSTANCE.dip2px(40), UiUtils.INSTANCE.dip2px(40));
        }

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        uCrop.withOptions(options);
        uCrop.start(getContext(), this);
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            File file = new File(getContext().getCacheDir(), destinationFileName);
            upLoad(file);
        } else {
            UiUtils.INSTANCE.showToast("裁剪图片失败");
        }
    }


    private LoadingDialog dialog;

    private void upLoad(File file) {
        if (dialog == null) {
            dialog = new LoadingDialog(getContext());
            dialog.setCancelable(false);
            dialog.setWaitText("请稍后..");
        }
        dialog.show();
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
        dialog.setWaitText(getString(R.string.tips_uploading));

        subscribe = RetrofitHelper.getApi().upload(imageBodyPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<UploadInfo>>(getActivity()) {
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
                    public void onNext(ResultBody<UploadInfo> info) {
                        dialog.dismiss();
                        if (info != null) {
                            if (info.code == 200) {
                                LogUtil.i("图片上传成功！path：" + info.data.image);
                                if (loadListener != null)
                                    loadListener.uploadImageSuccess(info.data.image);
                                dismiss();
                            } else {
                                UiUtils.INSTANCE.showToast(getString(R.string.tips_upload_failed));
                            }
                        } else {
                            UiUtils.INSTANCE.showToast(getString(R.string.tips_upload_failed));
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null) {
            if (!subscribe.isUnsubscribed())
                subscribe.unsubscribe();
        }
    }

    private onUpLoadListener loadListener;

    public onUpLoadListener getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(onUpLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public interface onUpLoadListener {
        void uploadImageSuccess(String path);
    }
}
