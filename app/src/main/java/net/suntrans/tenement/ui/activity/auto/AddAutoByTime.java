package net.suntrans.tenement.ui.activity.auto;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import net.suntrans.common.utils.UiUtils;
import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.ActivityAutoChoosetimeBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.AddActionFragment;
import net.suntrans.tenement.ui.fragment.auto.ChooseTimeFragment;

/**
 * Created by Looney on 2017/11/20.
 */

public class AddAutoByTime extends BasedActivity implements ChooseTimeFragment.OnFragmentInteractionListener, AddActionFragment.OnFragmentInteractionListener {

    private ActivityAutoChoosetimeBinding binding;
    private ChooseTimeFragment chooseTimeFragment;
    private AddActionFragment addActionFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_choosetime);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    // Lastly, it will rely on the system behavior for back
                    finish();
                }
            }
        });

        chooseTimeFragment = (ChooseTimeFragment) getSupportFragmentManager().findFragmentByTag("CHOSETIME");
        if (chooseTimeFragment == null) {
            chooseTimeFragment = new ChooseTimeFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.slide_in_right, R.anim.slide_out_left,
//                R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.content, chooseTimeFragment, "STEP1");
        transaction.commit();

    }


    public void rightSubTitleClicked(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            UiUtils.INSTANCE.showToast("您未添加任何动作");
        } else if (fragmentManager.getBackStackEntryCount() == 0) {
            navitiveToNextFragment();
            binding.title.setText("18:00");
        }else {

        }

    }

    private void navitiveToNextFragment() {
        addActionFragment = (AddActionFragment) getSupportFragmentManager().findFragmentByTag("ADD_ACTION");
        if (addActionFragment == null) {
            addActionFragment = new AddActionFragment();
        }
        if (addActionFragment != null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(
//                    R.anim.slide_in_right, R.anim.slide_out_left,
//                    R.anim.slide_in_left, R.anim.slide_out_right);
            if (addActionFragment.isAdded()) {
                transaction.show(addActionFragment);

            } else {
                transaction.add(R.id.content, addActionFragment, "ADD_ACTION");
                transaction.addToBackStack(null);
            }

            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Lastly, it will rely on the system behavior for back
            super.onBackPressed();
        }
    }


    @Override
    public void updateTitle(String title) {

    }
}
