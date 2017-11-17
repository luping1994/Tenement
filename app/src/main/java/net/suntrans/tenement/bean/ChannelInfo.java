package net.suntrans.tenement.bean;

/**
 * Created by Looney on 2017/11/13.
 * Des:
 */

public class ChannelInfo {
    /**
     * id : 1054
     * name : 通道2
     * number : 2
     * status : 0
     * device_type : 1
     * used : 1
     */

    public int id;
    public String name;
    public String datapoint_name;
    public String number;
    public int status;
    public String device_type;
    public String used;
    //以下属性仅用作选择通道时候
    public boolean checked =false;
}
