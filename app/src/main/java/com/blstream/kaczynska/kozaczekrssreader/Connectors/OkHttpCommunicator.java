package com.blstream.kaczynska.kozaczekrssreader.Connectors;

import com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider.IConnection;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCommunicator implements IConnection {
    OkHttpClient client;
    public OkHttpCommunicator() {
        client = new OkHttpClient();
    }

    @Override
    public String getResponse(String mBaseUrl) {
        String responseString = null;
        Response response = null;
        Request request = new Request.Builder()
                .url(mBaseUrl)
                .build();
        try {
            response = client.newCall(request).execute();
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}