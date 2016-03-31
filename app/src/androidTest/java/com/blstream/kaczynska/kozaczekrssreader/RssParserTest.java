package com.blstream.kaczynska.kozaczekrssreader;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RssParserTest {

    RssParser parser;

    @Before
    public void setup(){
        parser = new RssParser();
    }
    @Test
    public void shouldReturnNull_ResponseIsNull() throws Exception{
        //gven
        String response = null;
        //when
        Channel channel = parser.parse(response);
        //then
        assertNull(channel);
    }

    @Test
    public void shouldReturnChannel_ResponseNotNull() throws Exception{
        //given
        String Response = ""
    }
}