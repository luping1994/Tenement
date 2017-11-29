package net.suntrans.tenement.bean;

/**
 * Created by Looney on 2017/11/29.
 * Des:
 */

public class Monitor {

    /**
     * id : 1
     * name : 公司测试
     * electricity : 0
     * power : 0
     * current : 0
     * updated_at : 2017-11-21 10:20:04
     * label : {"value":"正常","color":"#FF0000"}
     */

    public String id;
    public String name;
    public String electricity;
    public String power;
    public String current;
    public String updated_at;
    public LabelBean label;

    public static class LabelBean {
        /**
         * value : 正常
         * color : #FF0000
         */

        public String value;
        public String color;
    }
}
