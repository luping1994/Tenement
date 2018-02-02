package net.suntrans.tenement.bean;

import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangEntity{


    /**
     * list : [{"id":1,"area_id":70,"name":"第六感","msg":"测试","created_at":"2018-01-31 17:30:49","updated_at":"2018-01-31 17:30:49"}]
     * current_page : 1
     * total_page : 1
     */

    public int current_page;
    public int total_page;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 1
         * area_id : 70
         * name : 第六感
         * msg : 测试
         * created_at : 2018-01-31 17:30:49
         * updated_at : 2018-01-31 17:30:49
         */

        public int id;
        public int area_id;
        public String name;
        public String msg;
        public String created_at;
        public String updated_at;
    }
}
