package com.netomarin.tablemountain.webservice;

import org.apache.http.HttpResponse;

public class RSSService {

    public static HttpResponse getNewsFeed(String feedUrl) {
        String host = feedUrl;
        String path = null;
        if (feedUrl.contains("/")) {
            int slashIndex = feedUrl.indexOf("/");
            host = feedUrl.substring(0, slashIndex);
            path = feedUrl.substring(slashIndex);
        }
        HttpResponse response = HTTPOperations.doGet(host, 80, path, null);
        return response;
    }
}