package com.blstream.kaczynska.kozaczekrssreader.Component;

import android.content.Context;

import com.blstream.kaczynska.kozaczekrssreader.Connectors.MyHttpConnection;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.MyUrlConnection;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.OkHttpCommunicator;
import com.blstream.kaczynska.kozaczekrssreader.Connectors.VolleyConnection;
import com.blstream.kaczynska.kozaczekrssreader.Module.ConnectionModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {

    MyHttpConnection provideConnection();
    OkHttpCommunicator provideOKHttpConnection();
    MyUrlConnection provideMyUrlConnection();
    VolleyConnection provideVolleyConnection();
}
