package net.suntrans.tenement.ui.fragment.admin

import android.content.Intent
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
import net.suntrans.tenement.ui.activity.AddMessageActivity
import net.suntrans.tenement.ui.fragment.rent.RentMineFragment

/**
 * Created by Looney 2017/11/08.
 */
class AdminMainFragment : Fragment() {


    private var binding: FragmentMainBinding? = null
    private var manager: FragmentManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        binding!!.toolbar.title.setText("三川物业")

        val homepageFragment = AdminHomepageFragment()
        val mineFragment = RentMineFragment()

        val adapter = FragmentAdapter(childFragmentManager)
        adapter.addFragment(homepageFragment, "首页")
        adapter.addFragment(mineFragment, "我的")
        binding!!.start.setOnClickListener {
            val intent = Intent(activity, AddMessageActivity::class.java)
            startActivity(intent)
        }

        binding!!.start.setImageResource(R.drawable.icon_fabu)
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