package net.suntrans.tenement.ui.fragment.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.FragmentCompanyinfoBinding;
import net.suntrans.tenement.ui.fragment.BasedFragment;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyInfoFragment extends BasedFragment {


    private FragmentCompanyinfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_companyinfo,container,false);
        return binding.getRoot();
    }


}
