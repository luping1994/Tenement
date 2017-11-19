package net.suntrans.tenement.bean;

/**
 * Created by Looney on 2017/11/20.
 */

public enum ActionType {
    EXCUTE_SCENE("执行某个场景"),TOGLE_AUTO("开关某条自动化"),SEND_MESSAGE("向手机发送一条信息");

    public String type;
    ActionType(String type) {
        this.type = type;
    }
}
