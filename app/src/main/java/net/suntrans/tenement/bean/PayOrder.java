package net.suntrans.tenement.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2018/1/26.
 * Des:
 */

public class PayOrder {

    /**
     * appid : wx9b41c2c9808d5c94
     * partnerid : 1492875852
     * prepayid : wx20180125114123de5647ef890513063608
     * noncestr : UAd1ZHqoxvXK48Aq
     * timestamp : 1516851683
     * package : Sign=WXPay
     * newsign : 33CA84133E524ACF91533CD1945F8F4E
     */

    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    @SerializedName("package")
    public String packageX;
    public String newsign;


    @Override
    public String toString() {
        return "PayOrder{" +
                "appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", packageX='" + packageX + '\'' +
                ", newsign='" + newsign + '\'' +
                '}';
    }
}
