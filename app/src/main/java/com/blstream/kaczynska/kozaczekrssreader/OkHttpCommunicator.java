package com.blstream.kaczynska.kozaczekrssreader;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCommunicator {

    OkHttpClient client;

    public OkHttpCommunicator() {
        client = new OkHttpClient();
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        return responseString;
    }
}
