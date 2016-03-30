package com.blstream.kaczynska.kozaczekrssreader.ConnectionProvider;


/**
 * Interface used to Connect to Service.
 */
public interface IConnection {
    /**
     *  Method used to get Response from Service.
     * @param mBaseUrl url to service as String.
     * @return Response from service as a String.
     */
    String getResponse(String mBaseUrl);

}
