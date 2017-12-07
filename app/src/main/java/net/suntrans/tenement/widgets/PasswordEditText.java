package net.suntrans.tenement.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.suntrans.looney.widgets.EditView;
import net.suntrans.tenement.R;


/**
 * Created by Looney on 2016/11/10.
 */


public class PasswordEditText extends ViewGroup implements View.OnClickListener {


    private ImageView eye;
    private EditView editView;

    private boolean visible = false;//密码是否为可见
    private int eyeHeight;
    private int eyeWidth;

    public PasswordEditText(@NonNull Context context) {
        this(context, null);
    }

    public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (editView == null) {
            ensureEditView();
        }
        if (editView == null) {
            return;
        }
        eye.measure(widthMeasureSpec, heightMeasureSpec);

        eyeHeight = eye.getMeasuredHeight();
        eyeWidth = eye.getMeasuredWidth();

        int editViewWidthSpc = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - eyeWidth, MeasureSpec.EXACTLY);
        int editViewHeightSpc = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - eyeHeight, MeasureSpec.EXACTLY);

        editView.measure(editViewWidthSpc, editViewHeightSpc);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }
        if (editView==null){
            return;
        }

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        editView.layout(getPaddingLeft(), getPaddingTop(), width - getPaddingRight() - eyeWidth, height - getPaddingBottom());
        eye.layout(width-getPaddingRight()-eyeWidth,getPaddingTop(),width,height-getPaddingBottom());
    }

    private void init() {
        eye = new ImageView(this.getContext());
        eye.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  //设置图片宽高
        eye.setImageResource(R.drawable.ic_eye_off); //图片资源
        addView(eye); //动态添加图片
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureEditView();
        if (editView != null) {
            editView.setOnMyFocusChangeListener(new EditView.onMyFocusChangeListener() {
                @Override
                public void onFocusChanged(View v, boolean hasFocus) {
                    if (hasFocus) {
                        eye.setVisibility(VISIBLE);
                    } else {
                        eye.setVisibility(INVISIBLE);
                    }
                }
            });
        }
        eye.setOnClickListener(this);
    }

    private void ensureEditView() {
        if (editView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (!eye.equals(view)) {
                    editView = (EditView) view;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eye:
                visible = !visible;
                if (visible) {
                    editView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.ic_eye_off);
                } else {
                    editView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.ic_eye);

                }

                break;
        }
    }

    public Editable getText() {
        return editView.getText();
    }

    public void setText(String text) {
        editView.setText(text);
    }
}
