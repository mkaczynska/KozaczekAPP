package com.blstream.kaczynska.kozaczekrssreader;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.blstream.kaczynska.kozaczekrssreader.Component.DaggerIConnectionComponent;
import com.blstream.kaczynska.kozaczekrssreader.Component.IConnectionComponent;
import com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider.IConnection;
import com.blstream.kaczynska.kozaczekrssreader.Module.ConnectionModule;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class RssPullService extends IntentService implements Constants {

    private static final String HTTP_CONNECTION = "HttpConnection";
    private static final String URL_CONNECTION = "UrlConnection";
    private static final String OK_HTTP_CONNECTION = "OkHttpConnection";
    private static final String VOLLEY_CONNECTION = "VolleyConnection";

    private  IConnectionComponent component;
    private  IConnection connection;


    public RssPullService() {
        super("RssService");
        component = DaggerIConnectionComponent
                .builder()
                .connectionModule(new ConnectionModule(this))
                .build();
        connection = component.provideConnection();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Channel rssChannel = null;
        String url = intent.getStringExtra(URL_ID);
        try {
            loadPreferences();
            String response = connection.getResponse(url);
            RssParser rssParser = new RssParser();
            rssChannel = rssParser.parse(response);

        } catch (XmlPullParserException | IOException e) {
            Log.w(e.getMessage(), e);
        }
        Intent localIntent = new Intent(INTENT_MAIN);
        localIntent.putExtra(CHANNEL, rssChannel);
        sendBroadcast(localIntent);
    }

    private void loadPreferences(){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String downloadType = SP.getString(getString(R.string.downloadType), getString(R.string.downloadValue));
        switch (downloadType){
            case HTTP_CONNECTION : connection = component.provideConnection();
                break;
            case URL_CONNECTION : connection = component.provideMyUrlConnection();
                break;
            case OK_HTTP_CONNECTION : connection = component.provideOKHttpConnection();
                break;
            case VOLLEY_CONNECTION : connection = component.provideVolleyConnection();
            default : connection = component.provideConnection();
        }
    }
}
