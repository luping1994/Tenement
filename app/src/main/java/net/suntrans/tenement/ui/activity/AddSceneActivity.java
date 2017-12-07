package net.suntrans.tenement.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.looney.widgets.IosAlertDialog;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.ChannelInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneItem;
import net.suntrans.tenement.databinding.ActivityAddSceneBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.fragment.PicChooseFragment;
import net.suntrans.tenement.ui.fragment.rent.AllChannelFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class AddSceneActivity extends BasedActivity implements PicChooseFragment.onItemChooseListener, AllChannelFragment.onChannelSelectedListener {

    private ActivityAddSceneBinding binding;
    private LoadingDialog dialog;
    private PicChooseFragment fragment;
    private String imgId = "1";
    private AllChannelFragment allChannelFragment;

    private SceneDetailActivity.SceneItemAdapter adapter;
    private List<SceneItem> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_scene);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.addDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allChannelFragment = new AllChannelFragment();
                allChannelFragment.setOnChannelSelectedListener(AddSceneActivity.this);
                allChannelFragment.show(getSupportFragmentManager(), "allChannel");
            }
        });

        binding.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PicChooseFragment();
                fragment.setOnItemChooseListener(AddSceneActivity.this);
                fragment.show(getSupportFragmentManager(), "choosePic");
            }
        });

        datas = new ArrayList<>();
        adapter = new SceneDetailActivity.SceneItemAdapter(R.layout.item_scene_channel, datas);
        adapter.bindToRecyclerView(binding.recyclerView);
        adapter.setEmptyView(R.layout.recyclerview_empty_view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showModifyDialog(position);
            }
        });
    }


    public void rightSubTitleClicked(View view) {
        createScene();
    }

    private void createScene() {
        String name = binding.sceneName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast("名称不能为空");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("保存中...");
            dialog.setCancelable(false);
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("image", imgId);
        if (datas.size()!=0){
            try {
                JSONArray array = new JSONArray();
                for (SceneItem item :
                        datas) {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("id", item.channel_id);
                    jsonObject.put("status", item.status);
                    array.put(jsonObject);

                }
                System.out.println(array.toString());
                map.put("channels", array.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        System.out.println(name + "," + imgId);

        addSubscription(api.createScene(map), new BaseSubscriber<ResultBody>(getApplicationContext()) {
            @Override
            public void onNext(ResultBody resultBody) {
                super.onNext(resultBody);
                UiUtils.showToast(resultBody.msg);
                dialog.dismiss();
                new IosAlertDialog(AddSceneActivity.this)
                        .builder()
                        .setMsg("创建成功!")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).setCancelable(false).show();
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onPicChoose(String id, String path) {
        UiUtils.showToast(id);
        if (fragment != null) {
            fragment.dismiss();
        }
        Glide.with(this)
                .load(path)
                .dontTransform()
                .crossFade()
                .override(UiUtils.dip2px(64), UiUtils.dip2px(64))
                .into(binding.sceneImg);
        imgId = id;
    }


    @Override
    public void onChannelSelected(List<ChannelInfo> items) {
        datas.clear();
        for (int i = 0; i < items.size(); i++) {
            SceneItem item = new SceneItem();
            item.channel_id = items.get(i).id;
            item.title = items.get(i).title;
            datas.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChannelSelected(String ids) {
        //do nothing
    }

    private void showModifyDialog(final int position) {
        String[] items = {"打开", "关闭", "删除"};
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            datas.get(position).status = 1;
                        } else if (which == 1) {
                            datas.get(position).status = 0;

                        } else if (which == 2) {
                            adapter.remove(position);
                        }
                        adapter.notifyDataSetChanged();
                        System.out.println(datas.size());
                    }
                }).create().show();
    }
}
