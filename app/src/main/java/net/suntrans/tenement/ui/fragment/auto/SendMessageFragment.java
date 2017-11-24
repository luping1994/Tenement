package net.suntrans.tenement.ui.fragment.auto;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.tenement.R;
import net.suntrans.tenement.databinding.FragmentSendMessageBinding;

public class SendMessageFragment extends Fragment {

    private FragmentSendMessageBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_message,container,false);
        return binding.getRoot();
    }


    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.updateTitle("向手机发送消息");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void updateTitle(String title);
    }

    public String getMessage(){
        return  binding.neirong.getText().toString();
    }


}
