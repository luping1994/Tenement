package net.suntrans.tenement;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/12/5.
 * Des:
 */

public class MessageType {
    public static final int NORMAL =1;
    public static final int IMPORTANT =2;
    public static final int URGENCY =3;

    public static Map<Integer,String> messageTips;
    public static Map<Integer,Integer> messageColor;
    static {
        messageTips = new HashMap<>();
        messageTips.put(NORMAL,"公告");
        messageTips.put(IMPORTANT,"重要");
        messageTips.put(URGENCY,"紧急");

        messageColor = new HashMap<>();
        messageColor.put(NORMAL, Color.parseColor("#50bff9"));
        messageColor.put(IMPORTANT, Color.parseColor("#f1b147"));
        messageColor.put(URGENCY, Color.parseColor("#f95350"));
    }
}
