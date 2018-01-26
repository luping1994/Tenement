package net.suntrans.tenement.ui.fragment.rent


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.suntrans.tenement.R
import net.suntrans.tenement.adapter.FragmentAdapter
import net.suntrans.tenement.databinding.FragmentMainBinding

/**
 * Created by Looney 2017/11/08.
 */
class RentMainFragment : Fragment() {


    private var binding: FragmentMainBinding? = null
    private var manager: FragmentManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        binding!!.toolbar.title.setText("三川物业")

        val homepageFragment = RentHomepageFragment()
//        val mineFragment = RentMineFragment()

        val adapter = FragmentAdapter(childFragmentManager)
        adapter.addFragment(homepageFragment, "首页")
//        adapter.addFragment(mineFragment, "我的")
//        binding!!.start.setOnClickListener { println("开始营业") }

        binding!!.viewPager.adapter = adapter
        manager = childFragmentManager

        binding!!.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_homepage -> binding!!.viewPager.setCurrentItem(0, false)
                R.id.item_mime -> binding!!.viewPager.setCurrentItem(1, false)
            }
            true
        }
    }


}
