package net.suntrans.tenement.bean;

import net.suntrans.tenement.persistence.User;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class LoginInfo {
    /**
     * token : {"token_type":"Bearer","expires_in":1296000,"access_token":"******","refresh_token":"******","expires_time":"2017-11-30 11:49:00"}
     * user : {"id":59,"company_id":3,"area_id":5,"username":"st0001","nickname":"主人","mobile":"132111","login":14,"manager":1,"cover":"","created_at":"2017-11-09 11:29:45","updated_at":"2017-11-15 11:45:49","company_name":"三川研发中心"}
     * timer : {"sensus":60000,"ammeter":600000,"light":3000}
     */

    public TokenBean token;
    public User user;
    public TimerBean timer;

    public static class TokenBean {
        /**
         * token_type : Bearer
         * expires_in : 1296000
         * access_token : ******
         * refresh_token : ******
         * expires_time : 2017-11-30 11:49:00
         */
        public String token_type;
        public int expires_in;
        public String access_token;
        public String refresh_token;
        public long expires_time;
    }



    public static class TimerBean {
        /**
         * sensus : 60000
         * ammeter : 600000
         * light : 3000
         */

        public int sensus;
        public int ammeter;
        public int light;
    }
}
