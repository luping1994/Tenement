package net.suntrans.tenement.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.Image;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneImage;
import net.suntrans.tenement.databinding.FragmentChoosePicBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Looney on 2017/11/17.
 * Des:
 */

public class PicChooseFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setTitle("选择图片");
        return dialog;
    }

    private FragmentChoosePicBinding binding;
    private SceneImageAdapter adapter;
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
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_pic, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        adapter = new SceneImageAdapter(R.layout.item_image, datas, getContext());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemChooseListener != null) {
                    onItemChooseListener.onPicChoose(datas.get(position).id, datas.get(position).path);
                }
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private onItemChooseListener onItemChooseListener;

    public void setOnItemChooseListener(PicChooseFragment.onItemChooseListener onItemChooseListener) {
        this.onItemChooseListener = onItemChooseListener;
    }


    public interface onItemChooseListener {
        void onPicChoose(String id, String path);
    }

    private List<Image> datas;

    private void getData() {
        mCompositeSubscription.add(api.getSceneDefaultImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<SceneImage>>(getContext()) {
                    @Override
                    public void onNext(ResultBody<SceneImage> channelEntityResultBody) {
                        datas.clear();
                        datas.addAll(channelEntityResultBody.data.lists);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    static class SceneImageAdapter extends BaseQuickAdapter<Image, BaseViewHolder> {

        private Context context;
        private int size;

        public SceneImageAdapter(int layoutResId, @Nullable List<Image> data, Context context) {
            super(layoutResId, data);
            this.context = context;
            this.size = UiUtils.INSTANCE.dip2px(24);
        }

        @Override
        protected void convert(BaseViewHolder helper, Image item) {
            ImageView view = helper.getView(R.id.image);

            Glide.with(context)
                    .load(item.path)
                    .dontTransform()
                    .placeholder(R.drawable.ic_sleep)
                    .crossFade()
                    .override(size, size)
                    .into(view);
        }
    }

}
