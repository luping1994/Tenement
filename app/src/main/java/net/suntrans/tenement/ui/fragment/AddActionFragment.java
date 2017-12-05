package net.suntrans.tenement.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.tenement.R;
import net.suntrans.tenement.adapter.DividerItemDecoration;
import net.suntrans.tenement.bean.AutoActionItem;
import net.suntrans.tenement.databinding.FragmentAddActionBinding;
import net.suntrans.tenement.ui.activity.auto.ChooseActionActivity;

import java.util.ArrayList;
import java.util.List;


public class AddActionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FragmentAddActionBinding binding;
    private ActionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_action, container, false);
        return binding.getRoot();
    }

    private ArrayList<AutoActionItem> datas;


    private int requestCode = 101;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        binding.chooseAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChooseActionActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });

        adapter = new ActionAdapter(R.layout.item_auto_action, datas);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 102) {
            AutoActionItem info = data.getParcelableExtra("data");
            datas.add(info);
            adapter.notifyDataSetChanged();
        }

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
        mListener.updateTitle("动作");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    class ActionAdapter extends BaseQuickAdapter<AutoActionItem, BaseViewHolder> {

        public ActionAdapter(int layoutResId, @Nullable List<AutoActionItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AutoActionItem item) {
            helper.setText(R.id.type, item.type)
                    .setText(R.id.des, item.des);
        }
    }

    public interface OnFragmentInteractionListener {
        void updateTitle(String title);
    }
}
