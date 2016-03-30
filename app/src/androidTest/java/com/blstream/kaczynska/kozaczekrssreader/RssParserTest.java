package com.blstream.kaczynska.kozaczekrssreader;

import org.junit.Test;

import static org.junit.Assert.*;

public class RssParserTest {

    @Test
    public void shouldReturnNull_ResponseIsNull() throws Exception{
        //gven
        RssParser parser = new RssParser();
        String response = null;
        //when
        Channel channel = parser.parse(response);
        //then
        assertNull(channel);
    }

    @Test
    public void shouldReturnChannel_ResponseNotNull() throws Exception{

    }
}