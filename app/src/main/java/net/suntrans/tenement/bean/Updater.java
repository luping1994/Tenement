package net.suntrans.tenement.bean;

/**
 * Created by Looney on 2017/12/13.
 * Des:
 */

public class Updater {

    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {

        public String buildBuildVersion;
        public String downloadURL;
        public String buildVersionNo;
        public String buildVersion;
        public String buildShortcutUrl;
        public String buildUpdateDescription;
    }
}
