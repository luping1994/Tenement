package net.suntrans.tenement.widgets;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class PressImageView extends android.support.v7.widget.AppCompatImageView {

    public PressImageView(Context context) {
        super(context);
    }

    public PressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    final ColorMatrix colorMatrix = new ColorMatrix(new float[] { 0.5F, 0, 0, 0, 0, 0, 0.5F, 0, 0, 0, 0, 0, 0.5F, 0, 0, 0, 0, 0, 1, 0, });

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        if (pressed) {
            getDrawable().setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        } else {
            getDrawable().setColorFilter(null);
        }
    }
}
