package com.blstream.kaczynska.kozaczekrssreader.Connectors;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider.IConnection;

public class VolleyConnection implements IConnection{
    Context context;
    String result=null;
    public VolleyConnection(Context context){
        this.context = context;
    }
    @Override
    public String getResponse(String mBaseUrl) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest req = new StringRequest(Request.Method.GET, mBaseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(req);
        while(result == null){
        }
        return result;
    }
}
