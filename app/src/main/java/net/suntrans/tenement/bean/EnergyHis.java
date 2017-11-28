package net.suntrans.tenement.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/28.
 * Des:
 */

public class EnergyHis {

    /**
     * id : 25
     * sno : 171170039909
     * voltage : 223.7
     * current : 1.62
     * power : 212.2
     * power_rate : 0.643
     * electricity : 105.68
     * updated_at : 2017-11-28 09:40:20
     * today : {"x":[1,2,3,4,5,6,7,8,9],"y":[0.01,0.02,0.01,0.01,0.01,0.02,0.01,0.02,0.12]}
     * month : {"x":[26,27],"y":[0,0.82]}
     * year : {"x":[11],"y":[0]}
     * date_d : 28
     * date_m : 11
     * date_y : 2017
     */

    public String id;
    public String sno;
    public String voltage;
    public String current;
    public String power;
    public String power_rate;
    public String electricity;
    public String updated_at;
    public List<HisItem> today;
    public List<HisItem> month;
    public List<HisItem> year;
    public String date_d;
    public String date_m;
    public String date_y;

    public static class HisItem {
        public int x;
        public float y;
    }

}
