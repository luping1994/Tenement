package net.suntrans.tenement.ui.fragment.admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.api.Api;
import net.suntrans.tenement.api.RetrofitHelper;
import net.suntrans.tenement.bean.CompanyInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.databinding.FragmentCompanyinfoBinding;
import net.suntrans.tenement.rx.BaseSubscriber;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import javax.xml.transform.Result;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class CompanyInfoFragment extends BasedFragment {


    private FragmentCompanyinfoBinding binding;
    private Api api;
    private String id;

    public static CompanyInfoFragment newInstance(String id) {
        CompanyInfoFragment fragment = new CompanyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_companyinfo, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        id = getArguments().getString("id");
        getData(id);
    }

    private void getData(String id) {
        if (api == null)
            api = RetrofitHelper.getApi();
        addSubscription(api.getComInfo(id), new BaseSubscriber<ResultBody<CompanyInfo>>() {
            @Override
            public void onNext(ResultBody<CompanyInfo> companyInfo) {
                super.onNext(companyInfo);
                binding.telephone.setText(companyInfo.data.telphone);
                binding.time.setText(companyInfo.data.date_start+"~"+companyInfo.data.date_end);
                binding.address.setText(companyInfo.data.address);
                binding.name.setText(companyInfo.data.name);
                binding.startEle.setText(companyInfo.data.electricty_start+"kW·h");
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
