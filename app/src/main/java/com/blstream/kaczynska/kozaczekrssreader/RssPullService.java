package com.blstream.kaczynska.kozaczekrssreader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class RssPullService extends IntentService implements Constants {

    private String url;
    private OkHttpCommunicator okHttpCommunicator = new OkHttpCommunicator();

    public RssPullService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        ArrayList<Item> rssItems = null;
        Channel rssChannel = null;
        url = intent.getStringExtra(URL_ID);
        try {
            String response = getInputString();
            RssParser rssParser = new RssParser();
            rssChannel = rssParser.parse(response);

        } catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
        Intent localIntent = new Intent(INTENT_MAIN);
        localIntent.putExtra(CHANNEL, rssChannel);
        sendBroadcast(localIntent);
    }

    public String getInputString() {
        String response;
        try {
            response = okHttpCommunicator.run(url);
        } catch (IOException e) {
            return null;
        }
        return response;
    }
}
