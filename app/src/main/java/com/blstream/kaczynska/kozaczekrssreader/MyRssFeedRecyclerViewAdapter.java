package com.blstream.kaczynska.kozaczekrssreader;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class MyRssFeedRecyclerViewAdapter extends RecyclerView.Adapter<MyRssFeedRecyclerViewAdapter.ViewHolder> implements Parcelable {

    //    private  ArrayList<Item> rssFeedList;
    private Channel rssChannel;
    private OnHeadlineSelectedListener onHeadlineSelectedListener;

    public MyRssFeedRecyclerViewAdapter(Channel receivedChannel) {
        this.rssChannel = receivedChannel;
    }

    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener onHeadlineSelectedListener) {
        this.onHeadlineSelectedListener = onHeadlineSelectedListener;
    }

    protected MyRssFeedRecyclerViewAdapter(Parcel in) {
        rssChannel = in.readParcelable(Channel.class.getClassLoader());
    }

    public static final Creator<MyRssFeedRecyclerViewAdapter> CREATOR = new Creator<MyRssFeedRecyclerViewAdapter>() {
        @Override
        public MyRssFeedRecyclerViewAdapter createFromParcel(Parcel in) {
            return new MyRssFeedRecyclerViewAdapter(in);
        }

        @Override
        public MyRssFeedRecyclerViewAdapter[] newArray(int size) {
            return new MyRssFeedRecyclerViewAdapter[size];
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_feed, parent, false);
        return new ViewHolder(view, onHeadlineSelectedListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int currentPosition) {
        if (!rssChannel.itemsList.isEmpty()) {
            Item rssFeed = rssChannel.itemsList.get(currentPosition);
            holder.title.setText(rssFeed.getTitle());
            holder.description.setText(rssFeed.getDescription());
            holder.publicationDate.setText(rssFeed.getPublicationDate());
            holder.link.setText(rssFeed.getLink());
            ImageManager.loadBitmap(rssFeed.getImageUrl(), rssFeed.getImageKey(), holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return rssChannel.itemsList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(rssChannel, flags);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View newsView;
        public final TextView title;
        public final TextView description;
        public final TextView publicationDate;
        public final ImageView image;
        public final TextView link;

        public ViewHolder(View view, final OnHeadlineSelectedListener listener) {
            super(view);
            newsView = view;
            title = (TextView) view.findViewById(R.id.rssTitle);
            description = (TextView) view.findViewById(R.id.rssDescription);
            publicationDate = (TextView) view.findViewById(R.id.rssPublicationDate);
            image = (ImageView) view.findViewById(R.id.rssImage);
            link = (TextView) view.findViewById(R.id.rssLink);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onHeaderSelected(getAdapterPosition());
                    }
                }
            });
        }
    }
}