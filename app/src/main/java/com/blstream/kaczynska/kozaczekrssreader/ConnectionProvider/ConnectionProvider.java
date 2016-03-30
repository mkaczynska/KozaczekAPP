package com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider;

import javax.inject.Inject;

public class ConnectionProvider {


    IConnection provider;

    @Inject
    public ConnectionProvider(IConnection provider){
        this.provider = provider;
    }

    public String getResponseAsStringFromUrl(String mBaseUrl){

        return provider.getResponse(mBaseUrl);
    }
}
