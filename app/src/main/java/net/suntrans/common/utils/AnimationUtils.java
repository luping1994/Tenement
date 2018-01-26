package net.suntrans.common.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Looney on 2018/1/26.
 * Des:
 */

public class AnimationUtils {
    /**
     * 缩小
     *
     * @param view
     */
    public static void zoomIn(final View view, float scale, float dist,boolean justY) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        if (justY){
            mAnimatorSet.play(mAnimatorTranslateY);
        }else {
            mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
            mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        }

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     *
     * @param view
     */
    public static void zoomOut(final View view, float scale,boolean justY) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        if (justY){
            mAnimatorSet.play(mAnimatorTranslateY);
        }else {
            mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
            mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        }

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

}
