package com.namh.podopener.parser;

/**
 * Created by namh on 2016-08-20.
 * @see http://i5on9i.blogspot.kr/2015/01/rss-feed-atom-parser.html
 */

import android.app.DownloadManager;
import android.util.Xml;

import com.namh.podopener.recyclerview.Pod;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PodRssParser implements Runnable {


    /**
     * XML parser
     * http://developer.android.com/training/basics/network-ops/xml.html
     */

    private static final String ns = null;  // namespace

    private static final String FEED_TAG = "rss";
    private static final String CHANNEL_TAG = "channel";
    private static final String ENTRY_TAG = "item";

    private static final String ENCLOSURE_TAG = "enclosure";
    private static final String PUBDATE_TAG = "pubDate";
    private static final String TITLE_TAG = "title";

    private static final int ITEM_LIMIT_MAX = 1000;
    private int mLimit = ITEM_LIMIT_MAX;

    private static final String SEQ_ID_TAG = "SeqID";
    private static final String Uno_TAG = "Uno";
    private static final String UserID_TAG = "UserID";
    private static final String USER_NAME_TAG = "UserNm";
    private static final String COMMENT_TAG = "Comment";
    private static final String REG_DATE_TAG = "RegDate";
    private static final String Rank_TAG = "Rank";



    public PodRssParser(int limit) {
        mLimit = limit;
    }

    /**
     * This parser for the podcast feed
     * itunes xml example : http://minicast.imbc.com/PodCast/pod.aspx?code=1000671100000100000
     *
     * @param in
     * @return {@link List}
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List entries = null;

        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(CHANNEL_TAG)) {
                entries = readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<Pod> entries = new ArrayList<Pod>();

        int count = 0;
        parser.require(XmlPullParser.START_TAG, ns, CHANNEL_TAG);
        while (count < mLimit
                && parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(ENTRY_TAG)) {
                entries.add(readEntry(parser));
                count++;
            } else {
                skip(parser);
            }
        }
        return entries;
    }



    // Parses the contents of an entry. If it encounters a username, comment, or regdate tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Pod readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, ENTRY_TAG);

        String title = null;
        String podUrl = null;
        String pubDate = null;

        int count = 0;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(TITLE_TAG)) {
                title = readTitle(parser);
            } else if (name.equals(ENCLOSURE_TAG)) {
                podUrl = readEnclosure(parser);
            } else if (name.equals(PUBDATE_TAG)) {
                pubDate = readPubDate(parser);
            } else {
                skip(parser);
            }
        }
        return new Pod(title, podUrl.trim(), pubDate, -1, DownloadManager.STATUS_FAILED);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TITLE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TITLE_TAG);

        return title;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, PUBDATE_TAG);
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PUBDATE_TAG);

        return date;

    }


    // Processes link tags in the feed.
    private String readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, ENCLOSURE_TAG);
        String tag = parser.getName();
        String podUrl = parser.getAttributeValue(null, "url");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, ENCLOSURE_TAG);
        return podUrl;
    }


    // For the tags username and comment, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            // CDATA is also treated, here.
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


    //--------------------------------------------------------
    @Override
    public void run() {
        //this.parse();
    }


    //--------------------------------------------------------
//    public List<Pod> getMessages(){
//        return this.messages;
//    }


}