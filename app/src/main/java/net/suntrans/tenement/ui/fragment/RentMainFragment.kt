package net.suntrans.tenement.ui.fragment


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import net.suntrans.tenement.R
import net.suntrans.tenement.adapter.FragmentAdapter
import net.suntrans.tenement.databinding.FragmentRentMainBinding

/**
 * Created by Looney 2017/11/08.
 */
class RentMainFragment : Fragment() {


    private var binding: FragmentRentMainBinding? = null
    private var manager: FragmentManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_main, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding!!.toolbar.title.setText("三川物业业主界面")

        val homepageFragment = HomepageFragment()
        val mineFragment = MineFragment()

        val adapter = FragmentAdapter(childFragmentManager)
        adapter.addFragment(homepageFragment,"首页")
        adapter.addFragment(mineFragment,"我的")
        binding!!.start.setOnClickListener { println("开始营业") }

        binding!!.viewPager.adapter = adapter
        manager = childFragmentManager

        binding!!.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_homepage -> binding!!.viewPager.setCurrentItem(0,false)
                R.id.item_mime -> binding!!.viewPager.setCurrentItem(1,false)
            }
            true
        }
    }


}
