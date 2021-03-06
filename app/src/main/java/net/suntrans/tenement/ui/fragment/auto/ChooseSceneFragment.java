package net.suntrans.tenement.ui.fragment.auto;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneInfo;
import net.suntrans.tenement.databinding.FragmentChooseSceneBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class ChooseSceneFragment extends BasedFragment {


    private List<SceneInfo> datas;
    private FragmentChooseSceneBinding binding;
    private SceneAdapter adapter;
    private static Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreatedView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_scene, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        adapter = new SceneAdapter(R.layout.item_choose_scene, datas,getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mListener.onSceneSelected(datas.get(position));
            }
        });
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.scene_header_view,null,false);
        adapter.setHeaderView(headerView);

    }

    @Override
    public void onResume() {

        getData();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    static class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        private Context context;
        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data,Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);

            Glide.with(context)
                    .load(item.image)
                    .centerCrop()
                    .override(UiUtils.dip2px(36), UiUtils.dip2px(36))
                    .crossFade()
                    .placeholder(R.drawable.ic_nopic)
                    .into(imageView);
        }
    }

    public void getData() {
        mCompositeSubscription.add(api.getScene()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<SceneEntity>>(getActivity().getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<SceneEntity> channelEntityResultBody) {
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

    boolean sending = false;

    public void sendOrder(String id) {

    }

    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SendMessageFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.updateTitle("执行某个场景");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void updateTitle(String title);
        void onSceneSelected(SceneInfo info);
    }
}
