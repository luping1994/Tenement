package net.suntrans.tenement.ui.fragment.auto;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.FragmentChooseTimeBinding;
import net.suntrans.tenement.ui.activity.BasedActivity;
import net.suntrans.tenement.ui.fragment.BasedFragment;

import java.util.ArrayList;
import java.util.List;

public class ChooseTimeFragment extends BasedFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private FragmentChooseTimeBinding binding;

    public ChooseTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_time, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.llrichu.setOnClickListener(this);
        binding.llriluo.setOnClickListener(this);
        binding.lldantian.setOnClickListener(this);
        binding.repeatTime.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private List<String> weeks = new ArrayList<>();
    private boolean[] itemStatus = new boolean[]{false, false, false, false, false, false, false};

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llrichu:
                binding.richu.setChecked(true);
                binding.riluo.setChecked(false);
                binding.dantian.setChecked(false);
                if (binding.timepicker.getVisibility() == View.VISIBLE) {
                    binding.timepicker.setVisibility(View.GONE);
                }
                break;
            case R.id.llriluo:
                binding.richu.setChecked(false);
                binding.riluo.setChecked(true);
                binding.dantian.setChecked(false);
                if (binding.timepicker.getVisibility() == View.VISIBLE) {
                    binding.timepicker.setVisibility(View.GONE);
                }
                break;
            case R.id.lldantian:
                binding.richu.setChecked(false);
                binding.riluo.setChecked(false);
                binding.dantian.setChecked(true);
                binding.timepicker.setVisibility(View.VISIBLE);
                break;
            case R.id.repeatTime:
                new AlertDialog.Builder(getActivity())
                        .setMultiChoiceItems(R.array.weekItem, itemStatus, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                int index = which + 1;

                                if (isChecked) {
                                    System.out.println("add:" + index);

                                    weeks.add(index + "");
                                } else {
                                    weeks.remove(index + "");
                                    System.out.println("remove:" + index);

                                }
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void updateTitle(String title);

    }
}
