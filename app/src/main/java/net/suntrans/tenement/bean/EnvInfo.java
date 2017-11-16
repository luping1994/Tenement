package net.suntrans.tenement.bean;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class EnvInfo {


    /**
     * wendu : {"name":"温度","value":"26.15","text":"舒适","unit":"°C","color":"#FFFFFF"}
     * shidu : {"name":"湿度","value":"66.5","text":"舒适","unit":"%Rh","color":"#FFFFFF"}
     * jiaquan : {"name":"甲醛","value":"0.000","text":"清洁","unit":"ug/m³","color":"#FFFFFF"}
     * pm25 : {"name":"pm2.5","value":"50.0","text":"良","unit":"ug/m³","color":"#FFFFFF"}
     */
    public EnvItem wendu;
    public EnvItem shidu;
    public EnvItem jiaquan;
    public EnvItem pm25;

    public static class EnvItem {
        /**
         * name : 湿度
         * value : 66.5
         * text : 舒适
         * unit : %Rh
         * color : #FFFFFF
         */

        public String name;
        public String value;
        public String text;
        public String unit;
        public String color;
    }


}
