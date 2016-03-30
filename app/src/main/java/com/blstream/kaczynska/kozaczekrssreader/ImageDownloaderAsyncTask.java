package com.blstream.kaczynska.kozaczekrssreader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class ImageDownloaderAsyncTask extends AsyncTask<ImageDownloaderParams, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    String imageUrl;
    String imageKey;
    ImageView imageView;

    public ImageDownloaderAsyncTask(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(ImageDownloaderParams... params) {

        imageUrl = params[0].imageUrl;
        imageKey = params[0].imageKey;
        imageView = params[0].imageView;

        return ImageManager.setupBitmap(imageUrl, imageKey, imageView);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        final ImageView imageView = imageViewReference.get();
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
