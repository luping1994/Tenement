package net.suntrans.tenement.widgets;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import net.suntrans.common.utils.StatusBarCompat;
import net.suntrans.tenement.R;

/**
 * Created by Looney on 2017/11/24.
 * Des:
 */

public class FullScreenDialog extends AppCompatDialog {
    public FullScreenDialog(Context context) {
        this(context, 0);
    }

    public FullScreenDialog(Context context, int theme) {
        super(context, theme);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    protected FullScreenDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            WindowManager m = window.getWindowManager();
            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            //设置dialog的宽度未matchparent,高度为去掉状态栏和actionbar的高度
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (d.getHeight()-getContext().getResources().getDimension(R.dimen.action_bar_size)- StatusBarCompat.getStatusBarHeight(getContext())));
            //设置弹出动画
            window.setWindowAnimations(R.style.dialogWindowAnim);
           //设置gravity
            window.setGravity(Gravity.BOTTOM);
        }
    }

}
