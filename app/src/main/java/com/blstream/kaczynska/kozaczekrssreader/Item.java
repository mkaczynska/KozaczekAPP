package com.blstream.kaczynska.kozaczekrssreader;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {


    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    private static int ID_VALUE = 1;
    private int id;
    private String title;
    private String description;
    private String publicationDate;
    private FeedImage image;
    private String link;

    public Item() {
        setId();
        image = new FeedImage(id);
    }

    protected Item(Parcel in) {
        title = in.readString();
        description = in.readString();
        publicationDate = in.readString();
        link = in.readString();
        image = in.readParcelable(FeedImage.class.getClassLoader());

    }

    public int getId() {
        return id;
    }

    public void setId() {
        id = ID_VALUE;
        ID_VALUE++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageUrl() {
        return image.getUrl();
    }

    public void setImageUrl(String imageName) {
        this.image.setUrl(imageName);
    }

    public String getImageKey() {
        return image.getKey();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(publicationDate);
        dest.writeString(link);
        dest.writeParcelable(image, flags);
    }

    @Override
    public String toString() {
        String itemSummary = id + ".element, foto: " + image + " title: " + title + ", description: " + description + ", link: " + link;
        return itemSummary;
    }
}