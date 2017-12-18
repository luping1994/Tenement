package net.suntrans.looney.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.suntrans.looney.R;


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
        int measureWidth = measureWidth(0, widthMeasureSpec);
        int measureHeight = measureHeight(0, heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        // 首先判断params.width的值是多少，有三种情况。
        //
        // 如果是大于零的话，及传递的就是一个具体的值，那么，构造MeasupreSpec的时候可以直接用EXACTLY。
        // 如果为-1的话，就是MatchParent的情况，那么，获得父View的宽度，再用EXACTLY来构造MeasureSpec。
        // 如果为-2的话，就是wrapContent的情况，那么，构造MeasureSpec的话直接用一个负数就可以了。
        LayoutParams layoutParams = eye.getLayoutParams();

        int eyeWidthSpc = 0;
        int eyeHeightSpc = 0;

        eyeWidthSpc = getWidthSpc(measureWidth, eyeWidthSpc, layoutParams);
        eyeHeightSpc = getHeightSpc(measureHeight, layoutParams, eyeHeightSpc);

        eye.measure(eyeWidthSpc, eyeHeightSpc);//测量眼睛控件


        int editViewWidthSpc = 0;
        int editViewHeightSpc = 0;

        LayoutParams params = editView.getLayoutParams();
        editViewWidthSpc = getWidthSpc(measureWidth, editViewWidthSpc, params);
        editViewHeightSpc = getHeightSpc(measureHeight, params, editViewHeightSpc);

        editView.measure(editViewWidthSpc, editViewHeightSpc);
        // 设置自定义的控件MyLayout的大小
        setMeasuredDimension(measureWidth, measureHeight);

    }

    //根据孩子的layoutparams和付清的高度以及父亲给的测量规则获取孩子的测量spc获取
    private int getHeightSpc(int measureHeight, LayoutParams layoutParams, int heightSpc) {
        if (layoutParams.height > 0) {
            heightSpc = MeasureSpec.makeMeasureSpec(layoutParams.height,
                    MeasureSpec.EXACTLY);
        } else if (layoutParams.height == -1) {
            heightSpc = MeasureSpec.makeMeasureSpec(measureHeight,
                    MeasureSpec.EXACTLY);
        } else if (layoutParams.height == -2) {
            heightSpc = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.AT_MOST);
        }
        return heightSpc;
    }

    private int getWidthSpc(int measureWidth, int widthSpc, LayoutParams params) {
        if (params.width > 0) {
            widthSpc = MeasureSpec.makeMeasureSpec(params.width,
                    MeasureSpec.EXACTLY);
        } else if (params.width == -1) {
            widthSpc = MeasureSpec.makeMeasureSpec(measureWidth,
                    MeasureSpec.EXACTLY);
        } else if (params.width == -2) {
            widthSpc = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
        }
        return widthSpc;
    }

    private int measureHeight(int size, int pHeightMeasureSpec) {
        int result = size;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(size, heightSize);
                break;
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }


    private int measureWidth(int size, int pWidthMeasureSpec) {
        int result = size;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
                result = Math.min(size, widthSize);
                break;
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }
        if (editView == null) {
            return;
        }
        int eyeWidth = eye.getMeasuredWidth();


        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = width - getPaddingRight() - eyeWidth;
        int bottom = height - getPaddingBottom();
        editView.layout(left, top, right, bottom);
        eye.layout(width - getPaddingRight() - eyeWidth, getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
    }

    private void init() {
        eyePadding = dip2px(8,getContext());
        eye = new ImageView(this.getContext());
        eye.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  //设置图片宽高
        eye.setImageResource(R.drawable.ic_eye_off); //图片资源
//        eye.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        eye.setPadding(eyePadding,eyePadding,eyePadding,eyePadding);
        addView(eye); //动态添加图片
        if (visible) {
            eye.setVisibility(VISIBLE);
        } else {
            eye.setVisibility(INVISIBLE);
        }
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
        if (visible) {
            editView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.ic_eye_off);
        } else {
            editView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.ic_eye);
        }
        visible = !visible;
    }

    private int eyePadding =0;
    public static int dip2px(float dip, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


}
