package com.blstream.kaczynska.kozaczekrssreader;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedImage implements Parcelable {
    public static final Creator<FeedImage> CREATOR = new Creator<FeedImage>() {
        @Override
        public FeedImage createFromParcel(Parcel in) {
            return new FeedImage(in);
        }

        @Override
        public FeedImage[] newArray(int size) {
            return new FeedImage[size];
        }
    };
    private int id;
    private String url;
    private String key;
    private String KEY_BASIS = "img";

    public FeedImage(int id) {
        this.id = id;
        this.key = KEY_BASIS + id;
    }

    protected FeedImage(Parcel in) {
        id = in.readInt();
        url = in.readString();
        key = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(key);
    }
}
