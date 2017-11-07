package net.suntrans.tenement.ui.fragment


import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import net.suntrans.tenement.MainActivity

import net.suntrans.tenement.R
import net.suntrans.tenement.databinding.FragmentLoginBinding


/**
 * login fragment
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 * Create by Looney on 2017/11/6
 */
class LoginFragment : Fragment() {
    var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        binding!!.login.setOnClickListener({
            startActivity (Intent(activity, MainActivity::class.java))
        })
//        controlKeyboardLayout(binding!!.scrollView,binding!!.login)
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * @param root
     * 最外层布局，需要调整的布局
     * @param scrollToView
     * 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private fun controlKeyboardLayout(root: View, scrollToView: View) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val rect = Rect()
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect)
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        val rootInvisibleHeight = root.rootView
                                .height - rect.bottom
                        Log.i("tag", "最外层的高度" + root.rootView.height)
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 100) {
                            //软键盘弹出来的时候
                            val location = IntArray(2)
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location)
                            // 计算root滚动高度，使scrollToView在可见区域的底部
                            val srollHeight = location[1] + scrollToView
                                    .height - rect.bottom
                            root.scrollTo(0, srollHeight)
                        } else {
                            // 软键盘没有弹出来的时候
                            root.scrollTo(0, 0)
                        }
                    }
                })
    }
    }
