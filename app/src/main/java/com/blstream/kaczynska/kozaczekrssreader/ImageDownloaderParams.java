package com.blstream.kaczynska.kozaczekrssreader;

import android.widget.ImageView;

public class ImageDownloaderParams {
    String imageUrl;
    String imageKey;
    ImageView imageView;

    public ImageDownloaderParams(String imageUrl, String imageKey, ImageView imageView) {
        this.imageUrl = imageUrl;
        this.imageKey = imageKey;
        this.imageView = imageView;
    }
}
