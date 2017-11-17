package net.suntrans.tenement.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivitySceneBinding;
import net.suntrans.tenement.ui.fragment.SceneFragment;
import net.suntrans.tenement.ui.fragment.SceneManagerFragment;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */
public class SceneActivity extends BasedActivity {

    private ActivitySceneBinding binding;
    private SceneManagerFragment sceneManagerFragment;
    private SceneFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scene);
        fragment = new SceneFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.sceneContent, fragment).commit();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void rightSubTitleClicked(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            navitiveToNextFragment();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void navitiveToNextFragment() {
        sceneManagerFragment = (SceneManagerFragment) getSupportFragmentManager().findFragmentByTag("SceneManagerFragment");
        if (sceneManagerFragment == null) {
            sceneManagerFragment = new SceneManagerFragment();
        }
        if (sceneManagerFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.sceneContent, sceneManagerFragment, "SceneManagerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
            fragment.getData();
            binding.rightSubTitle.setText("管理");
        }else {
            binding.rightSubTitle.setText("完成");
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
