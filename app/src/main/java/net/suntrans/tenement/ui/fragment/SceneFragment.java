package net.suntrans.tenement.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.SceneInfo;
import net.suntrans.tenement.databinding.FragmentSceneBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SceneFragment extends RxFragment {


    private List<SceneInfo> datas;
    private FragmentSceneBinding binding;
    private SceneAdapter adapter;
    private QMUITipDialog tipsDialog;
    private static Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getData();
        adapter = new SceneAdapter(R.layout.item_scene, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position==datas.size()-1){
                    UiUtils.INSTANCE.showToast(getActivity().getApplicationContext(),"添加暂未实现");
                    return;
                }
                if (tipsDialog == null)
                    tipsDialog = new QMUITipDialog.Builder(getContext())
                            .setTipWord("已经为您执行")
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                            .create();
                tipsDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tipsDialog != null)
                            tipsDialog.dismiss();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    static class SceneAdapter extends BaseQuickAdapter<SceneInfo, BaseViewHolder> {

        public SceneAdapter(int layoutResId, @Nullable List<SceneInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneInfo item) {
            helper.setText(R.id.name, item.name);
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.resId);
        }
    }

    private void getData() {
        String[] sceneNames = {"睡眠", "一键关灯", "回家模式", "起床", "添加"};
        int[] resIds = {R.drawable.ic_sleep, R.drawable.ic_sleep, R.drawable.ic_sleep, R.drawable.ic_sleep, R.drawable.ic_add_scene};
        datas = new ArrayList<>();
        for (int i = 0; i < sceneNames.length; i++) {
            SceneInfo info = new SceneInfo();
            info.name = sceneNames[i];
            info.resId = resIds[i];
            datas.add(info);
        }
    }
}
