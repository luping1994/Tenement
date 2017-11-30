package net.suntrans.tenement.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/30.
 * Des:
 */

public class NoticeEntity {

    /**
     * total : 1
     * lists : [{"id":2,"vtype":1,"title":"电梯检修通知","created_at":"2017-11-30","url":"http://gzfhq.suntrans-cloud.com/app/notice/detail/2"}]
     */

    public String total;
    public List<NoticeItem> lists;

    public static class NoticeItem {
        /**
         * id : 2
         * vtype : 1
         * title : 电梯检修通知
         * created_at : 2017-11-30
         * url : http://gzfhq.suntrans-cloud.com/app/notice/detail/2
         */

        public String id;
        public String vtype;
        public String title;
        public String created_at;
        public String url;
    }
}
