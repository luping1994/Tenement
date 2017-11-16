package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneItem;
import net.suntrans.tenement.bean.SceneItemlEntity;
import net.suntrans.tenement.databinding.ActivitySceneDetailBinding;
import net.suntrans.tenement.rx.BaseSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */

public class SceneDetailActivity extends BasedActivity {

    ActivitySceneDetailBinding binding;
    private String sceneID;

    private List<SceneItem> datas;
    private SceneItemAdapter adapter;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene_detail);
        String sceneName = getIntent().getStringExtra("sceneName");
        sceneID = getIntent().getStringExtra("sceneID");
        binding.title.setText(sceneName);
        binding.sceneName.setText(sceneName);
        datas = new ArrayList<>();
        adapter = new SceneItemAdapter(R.layout.item_scene_channel, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void rightSubTitleClicked(View view) {
        updateScene();
    }

    private void updateScene() {
        String name = binding.sceneName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UiUtils.INSTANCE.showToast("名称不能为空");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("保存中...");
            dialog.setCancelable(false);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", sceneID);
        map.put("name", name);
        addSubscription(api.updateSceneInfo(map), new BaseSubscriber<ResultBody>(getApplicationContext()) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                UiUtils.INSTANCE.showToast(resultBody.msg);
                dialog.dismiss();
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.dismiss();

            }
        });
    }

    static class SceneItemAdapter extends BaseQuickAdapter<SceneItem, BaseViewHolder> {

        public SceneItemAdapter(int layoutResId, @Nullable List<SceneItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneItem item) {
            helper.setText(R.id.name, item.name)
                    .setText(R.id.status, item.status);
        }
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        mCompositeSubscription.add(api.getSceneChannel(sceneID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResultBody<SceneItemlEntity>>(this.getApplicationContext()) {
                    @Override
                    public void onNext(ResultBody<SceneItemlEntity> channelEntityResultBody) {
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

}
