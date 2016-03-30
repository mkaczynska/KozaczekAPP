package com.blstream.kaczynska.kozaczekrssreader.Module;


import android.content.Context;

import com.blstream.kaczynska.kozaczekrssreader.Connectors.MyHttpConnection;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.MyUrlConnection;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.OkHttpCommunicator;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.VolleyConnection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ConnectionModule {

    private final Context context;

    public ConnectionModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    MyHttpConnection provideHttpConnection(){
        return new MyHttpConnection();
    }

    @Provides
    @Singleton
    OkHttpCommunicator provideOkHttpConnection(){
        return new OkHttpCommunicator();
    }

    @Provides
    @Singleton
    MyUrlConnection provideMyUrlConnection(){
        return new MyUrlConnection();
    }

    @Provides
    @Singleton
    VolleyConnection provideVolleyConnection(){return new VolleyConnection(context);}
}
