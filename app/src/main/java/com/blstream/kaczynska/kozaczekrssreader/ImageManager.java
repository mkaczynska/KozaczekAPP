package com.blstream.kaczynska.kozaczekrssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageManager {

    static LRUCacheManager lruCacheManager = new LRUCacheManager();

    public static Bitmap setupBitmap(String imageUrl, String imageKey, ImageView imageView) {

        Bitmap bitmap = lruCacheManager.getBitmapFromMemCache(imageKey);
        if (bitmap == null) {
            bitmap = getBitmapFromUrl(imageUrl);
            lruCacheManager.addBitmapToMemoryCache(imageKey, bitmap);
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromUrl(String imageUrl) {
        Bitmap resizedBitmap = null;
        try {
            InputStream fileInputStream = (InputStream) new URL(imageUrl).getContent();
            resizedBitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizedBitmap;
    }

    public static void loadBitmap(String imageUrl, String imageKey, ImageView imageView) {

        ImageDownloaderParams asyncParams = new ImageDownloaderParams(imageUrl, imageKey, imageView);
        ImageDownloaderAsyncTask oldAsyncLoaderTask = (ImageDownloaderAsyncTask) imageView.getTag();
        if (oldAsyncLoaderTask != null) {
            oldAsyncLoaderTask.cancel(true);
        }
        ImageDownloaderAsyncTask asyncLoaderTask = new ImageDownloaderAsyncTask(imageView);
        asyncLoaderTask.execute(asyncParams);
        imageView.setTag(asyncLoaderTask);
    }
}
