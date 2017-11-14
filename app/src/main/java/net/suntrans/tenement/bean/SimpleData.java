package net.suntrans.tenement.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by Looney on 2017/11/14.
 * Des:
 */

public class SimpleData {

    public String name;

    @DrawableRes
    public int imgResId;

    public SimpleData(int imgResId, String name) {
        this.imgResId = imgResId;
        this.name = name;
    }
}
