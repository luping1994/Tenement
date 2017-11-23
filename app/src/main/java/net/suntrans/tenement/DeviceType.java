package net.suntrans.tenement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2017/11/21.
 * Des:
 */

public class DeviceType {

    public static final int LIGHT = 1;
    public static final int SOCKET = 2;
    public static final int AIR_CONDITIONER = 3;
    public static final int FRIDGE = 4;
    public static final int WATER = 5;
    public static final int MICRO_WAVE = 6;
    public static final int PRINTER = 7;
    public static Map<Integer, Integer> deviceIcons = new HashMap<>();

    static {
        deviceIcons.put(LIGHT, R.drawable.ic_light);
        deviceIcons.put(SOCKET, R.drawable.ic_socket);
        deviceIcons.put(AIR_CONDITIONER, R.drawable.ic_kongtiao);
        deviceIcons.put(FRIDGE, R.drawable.ic_bingxiang);
        deviceIcons.put(WATER, R.drawable.ic_yinshuiji);
        deviceIcons.put(MICRO_WAVE, R.drawable.ic_weibolu);
        deviceIcons.put(PRINTER, R.drawable.ic_dayinji);
    }
}
