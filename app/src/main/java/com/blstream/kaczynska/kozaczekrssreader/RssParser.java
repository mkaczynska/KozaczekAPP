package com.blstream.kaczynska.kozaczekrssreader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;


public class RssParser implements Constants {
    XmlPullParser xpp;
    Channel rssChannel = new Channel();

    public Channel parse(String in)
            throws XmlPullParserException, IOException {
        if (in != null) {
            int eventType = setupParser(in);
            Item currentItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                currentItem = addFeed(eventType, currentItem);
                eventType = xpp.next();
            }
        }
        return rssChannel;
    }

    private int setupParser(String in) throws XmlPullParserException, UnsupportedEncodingException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        xpp = factory.newPullParser();
        xpp.setInput(new StringReader(in));

        return xpp.getEventType();
    }

    private Item addFeed(int eventType, Item currentItem) throws IOException, XmlPullParserException {
        String name;
        if (eventType == XmlPullParser.START_TAG) {
            name = xpp.getName();
            currentItem = parseItem(name, currentItem);
        } else if (eventType == XmlPullParser.END_TAG) {
            name = xpp.getName();
            if (name.equalsIgnoreCase(ITEM_TAG) && currentItem != null) {
                rssChannel.addItem(currentItem);
                System.out.println(currentItem);
            }
        }
        return currentItem;
    }

    private Item parseItem(String name, Item currentItem) throws IOException, XmlPullParserException {
        if (name.equals(CHANNEL_TAG)) {
            xpp.next();
            if (xpp.getName().equals(TITLE_TAG)) {
                rssChannel.setChannelTitle(xpp.nextText());
            }
        } else if (name.equals(ITEM_TAG)) {
            currentItem = new Item();
        } else if (currentItem != null) {
            try {
                parseTag(name, currentItem);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        return currentItem;
    }

    private void parseTag(String name, Item currentItem) throws IOException, XmlPullParserException {
        if (name.equals(TITLE_TAG)) {
            currentItem.setTitle(xpp.nextText());
        } else if (name.equals(DESCRIPTION_TAG)) {
            currentItem.setDescription(xpp.nextText());
        } else if (name.equals(PUBLICATION_DATE_TAG)) {
            currentItem.setPublicationDate(xpp.nextText());
        } else if (name.equals(LINK_TAG)) {
            String currentElement = xpp.nextText();
            currentItem.setLink(currentElement);
        } else if (name.equals(IMAGE_TAG)) {
            currentItem.setImageUrl(xpp.getAttributeValue(null, URL_TAG));
        }
    }
}
