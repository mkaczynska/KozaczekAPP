package com.blstream.kaczynska.kozaczekrssreader;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Channel implements Parcelable { // FIXME brak formatowania
    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
    public ArrayList<Item> itemsList = new ArrayList<>();
    private String channelTitle;
    private String actualisationDate;

    public Channel() {
    }

    protected Channel(Parcel in) {
        channelTitle = in.readString();
        actualisationDate = in.readString();
        itemsList = in.createTypedArrayList(Item.CREATOR);
    }

    private boolean isItemAlreadyAdded(String link) {
        for (Item item : itemsList) {
            if (item.getLink().equals(link)) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Item newItem) {
        if (!isItemAlreadyAdded(newItem.getLink())) {
            newItem.setId();
            itemsList.add(newItem);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelTitle);
        dest.writeString(actualisationDate);
        dest.writeTypedList(itemsList);
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getActualisationDate() {
        return actualisationDate;
    }

    public void setActualisationDate(String actualisationDate) {
        this.actualisationDate = actualisationDate;
    }
}
