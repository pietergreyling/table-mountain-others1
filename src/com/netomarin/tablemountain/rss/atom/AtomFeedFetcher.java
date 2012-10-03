package com.netomarin.tablemountain.rss.atom;

import com.netomarin.tablemountain.webservice.RSSService;

import org.apache.http.HttpResponse;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class AtomFeedFetcher {

    private String feedUrl;
    private Feed feed;
    private InputStream isFeed;
    
    public AtomFeedFetcher(String feedUrl) {
        this.feedUrl = feedUrl;
    }
    
    public Feed fetchFeed() {
        getFeedData();
        if (isFeed != null) {
            AtomXMLParser parser = new AtomXMLParser();
            try {
                this.feed = parser.parse(isFeed);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.feed;
    }

    private void getFeedData() {
        HttpResponse response = RSSService.getNewsFeed(feedUrl);
        try {
            isFeed = response.getEntity().getContent();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}