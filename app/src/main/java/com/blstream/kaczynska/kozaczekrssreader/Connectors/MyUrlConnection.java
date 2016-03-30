package com.blstream.kaczynska.kozaczekrssreader.Connectors;

import com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider.IConnection;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class MyUrlConnection implements IConnection {
    private static final String ENCODING_STANDARD = "ISO-8859-2";

    @Override
    public String getResponse(String mBaseUrl) {
        InputStream inputStream;
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(mBaseUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return IOUtils.toString(inputStream, ENCODING_STANDARD);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
