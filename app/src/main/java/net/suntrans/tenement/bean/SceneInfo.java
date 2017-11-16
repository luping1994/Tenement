package net.suntrans.tenement.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2017/11/15.
 * Des:
 */

public class SceneInfo implements Parcelable {
    public String name;
    public String id;
    public String image;
    public String img_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeString(this.img_url);
    }

    public SceneInfo() {
    }

    protected SceneInfo(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.image = in.readString();
        this.img_url = in.readString();
    }

    public static final Parcelable.Creator<SceneInfo> CREATOR = new Parcelable.Creator<SceneInfo>() {
        @Override
        public SceneInfo createFromParcel(Parcel source) {
            return new SceneInfo(source);
        }

        @Override
        public SceneInfo[] newArray(int size) {
            return new SceneInfo[size];
        }
    };
}
