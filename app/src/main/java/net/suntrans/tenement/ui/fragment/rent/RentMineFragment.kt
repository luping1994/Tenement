package net.suntrans.tenement.ui.fragment.rent

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.suntrans.tenement.R
import net.suntrans.tenement.databinding.FragmentMineBinding

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

class RentMineFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentMineBinding? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_mine, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        binding!!.linearLayout.setOnClickListener(this)
        binding!!.message.setOnClickListener(this)
        binding!!.myShop.setOnClickListener(this)
        binding!!.contacts.setOnClickListener(this)
        binding!!.feedback.setOnClickListener(this)
        binding!!.aboutThis.setOnClickListener(this)
        binding!!.setting.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.linearLayout -> {
            }
            R.id.message -> {
            }
            R.id.myShop -> {
            }
            R.id.contacts -> {
            }
            R.id.feedback -> {
            }
            R.id.aboutThis -> {
            }
            R.id.setting -> {
            }
        }
    }
}
