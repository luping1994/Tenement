package net.suntrans.tenement.bean;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class EnvItem implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.value);
            dest.writeString(this.text);
            dest.writeString(this.unit);
            dest.writeString(this.color);
        }

        public EnvItem() {
        }

        protected EnvItem(Parcel in) {
            this.name = in.readString();
            this.value = in.readString();
            this.text = in.readString();
            this.unit = in.readString();
            this.color = in.readString();
        }

        public static final Parcelable.Creator<EnvItem> CREATOR = new Parcelable.Creator<EnvItem>() {
            @Override
            public EnvItem createFromParcel(Parcel source) {
                return new EnvItem(source);
            }

            @Override
            public EnvItem[] newArray(int size) {
                return new EnvItem[size];
            }
        };
    }


}
