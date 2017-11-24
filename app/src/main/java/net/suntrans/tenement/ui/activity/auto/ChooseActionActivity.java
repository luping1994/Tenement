package net.suntrans.tenement.ui.activity.auto;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.bean.ActionType;
import net.suntrans.tenement.bean.AutoActionItem;
import net.suntrans.tenement.bean.SceneInfo;
import net.suntrans.tenement.databinding.ActivityAutoChooseActionBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.auto.ChooseSceneFragment;
import net.suntrans.tenement.ui.fragment.auto.SendMessageFragment;

/**
 * Created by Looney on 2017/11/20.
 */

public class ChooseActionActivity extends BasedActivity implements
        View.OnClickListener, SendMessageFragment.OnFragmentInteractionListener
        , ChooseSceneFragment.OnFragmentInteractionListener {

    private ActivityAutoChooseActionBinding binding;
    private SendMessageFragment fragment;

    private ActionType actionType;
    private AutoActionItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_choose_action);
        binding.excuteScene.setOnClickListener(this);
        binding.togleAuto.setOnClickListener(this);
        binding.sendMessage.setOnClickListener(this);
        findViewById(R.id.back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.excuteScene:
                actionType = ActionType.EXCUTE_SCENE;
                ChooseSceneFragment chooseSceneFragment = (ChooseSceneFragment) getSupportFragmentManager().findFragmentByTag("chooseScene");
                if (chooseSceneFragment == null) {
                    chooseSceneFragment = new ChooseSceneFragment();
                }
                navitiveToNextFragment(chooseSceneFragment);

                break;
            case R.id.togleAuto:
                actionType = ActionType.TOGLE_AUTO;
                UiUtils.INSTANCE.showToast("未实现");
                break;
            case R.id.sendMessage:
                actionType = ActionType.SEND_MESSAGE;
                fragment = (SendMessageFragment) getSupportFragmentManager().findFragmentByTag("sendMessage");
                if (fragment == null) {
                    fragment = new SendMessageFragment();
                }
                navitiveToNextFragment(fragment);
                break;
        }
    }

    public void rightSubTitleClicked(View view) {
        switch (actionType) {
            case TOGLE_AUTO:
                break;
            case EXCUTE_SCENE:
                break;
            case SEND_MESSAGE:
                if (TextUtils.isEmpty(fragment.getMessage())) {
                    UiUtils.INSTANCE.showToast("请输入内容");
                    return;
                }
                item = new AutoActionItem();
                item.type = actionType.type;
                item.des = fragment.getMessage();
                Intent intent = new Intent();
                intent.putExtra("data", item);
                setResult(102, intent);
                finish();
                break;
        }
    }

    private void navitiveToNextFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.slide_in_right, R.anim.slide_out_left,
//                R.anim.slide_in_left, R.anim.slide_out_right);
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.content, fragment, "ADD_ACTION");
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    @Override
    public void updateTitle(String title) {
        binding.title.setText(title);
        if (title.equals("执行某个场景")) {
            binding.rightSubTitle.setVisibility(View.INVISIBLE);
        } else {
            binding.rightSubTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSceneSelected(SceneInfo info) {
        AutoActionItem autoActionItem = new AutoActionItem();
        autoActionItem.type = ActionType.EXCUTE_SCENE.type;
        autoActionItem.des = info.name;
        Intent intent = new Intent();
        intent.putExtra("data", autoActionItem);
        setResult(102, intent);
        finish();
    }


    private final FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    updateSubButton();
                }
            };

    private void updateSubButton() {
        boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
        if (isRoot){
            binding.title.setText("添加动作");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);

    }
}
