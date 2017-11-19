package net.suntrans.tenement.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/11/20.
 */

public class AutoActionItem implements Parcelable {
    public String type;
    public String name;
    public String des;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.des);
    }

    public AutoActionItem() {
    }

    protected AutoActionItem(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.des = in.readString();
    }

    public static final Parcelable.Creator<AutoActionItem> CREATOR = new Parcelable.Creator<AutoActionItem>() {
        @Override
        public AutoActionItem createFromParcel(Parcel source) {
            return new AutoActionItem(source);
        }

        @Override
        public AutoActionItem[] newArray(int size) {
            return new AutoActionItem[size];
        }
    };
}
