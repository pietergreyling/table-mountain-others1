
package com.netomarin.tablemountain.data;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.netomarin.tablemountain.commons.Constants;
import com.netomarin.tablemountain.provider.RSSProvider.FEED;
import com.netomarin.tablemountain.rss.atom.Author;
import com.netomarin.tablemountain.rss.atom.Feed;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FeedDAO {

    public static final String FEED_TABLE = "FEED";

    public static final String _ID = "_ID";
    public static final String FEED_ID = "FEED_ID";
    public static final String UPDATED = "UPDATED";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String TITLE = "TITLE";
    public static final String SUBTITLE = "SUBTITLE";
    public static final String ALT_LINK = "ALT_LINK";
    public static final String NEXT_LINK = "NEXT_LINK";
    public static final String AUTHOR = "AUTHOR";
    public static final String TOTAL_RESULTS = "TOTAL_RESULTS";
    public static final String START_INDEX = "START_INDEX";
    public static final String ITEMS_PER_PAGE = "ITEMS_PER_PAGE";

    public static ContentValues toContentValues(Feed feed) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATTER_PATTERN);
        ContentValues values = new ContentValues();
        values.put(FEED._ID, feed.get_id());
        values.put(FEED.FEED_ID, feed.getId());

        if (feed.getUpdated() != null)
            values.put(FEED.UPDATED, formatter.format(feed.getUpdated()));

        if (feed.getCategories() != null && feed.getCategories().size() > 0)
            values.put(FEED.CATEGORIES, new JSONArray(feed.getCategories()).toString());

        values.put(FEED.TITLE, feed.getTitle());
        values.put(FEED.SUBTITLE, feed.getSubtitle());
        values.put(FEED.ALT_LINK, feed.getAlternateLink());
        values.put(FEED.NEXT_LINK, feed.getNextLink());

        if (feed.getAuthor() != null) {
            Gson gson = new Gson();
            values.put(FEED.AUTHOR, gson.toJson(feed.getAuthor()));
        }

        values.put(FEED.TOTAL_RESULTS, feed.getTotalResults());
        values.put(FEED.START_INDEX, feed.getStartIndex());
        values.put(FEED.ITEMS_PER_PAGE, feed.getItemsPerPage());

        return values;
    }

    public static Feed fromContentValues(ContentValues values) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATTER_PATTERN);
        Feed feed = new Feed();
        feed.set_id(values.getAsLong(FEED._ID));
        feed.setId(values.getAsString(FEED.FEED_ID));

        try {
            feed.setUpdated(formatter.parse(values.getAsString(FEED.UPDATED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (values.containsKey(FEED.CATEGORIES)) {
            try {
                JSONArray jsonArray = new JSONArray(values.getAsString(FEED.CATEGORIES));
                for (int i = 0; i < jsonArray.length(); i++) {
                    feed.addCategory(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        feed.setTitle(values.getAsString(FEED.TITLE));
        feed.setSubtitle(values.getAsString(FEED.SUBTITLE));
        feed.setAlternateLink(values.getAsString(FEED.ALT_LINK));
        feed.setNextLink(values.getAsString(FEED.NEXT_LINK));

        if (values.containsKey(FEED.AUTHOR)) {
            Gson gson = new Gson();
            feed.setAuthor(gson.fromJson(values.getAsString(FEED.AUTHOR), Author.class));
        }

        feed.setTotalResults(values.getAsInteger(FEED.TOTAL_RESULTS));
        feed.setStartIndex(values.getAsInteger(FEED.START_INDEX));
        feed.setItemsPerPage(values.getAsInteger(FEED.ITEMS_PER_PAGE));

        return feed;
    }
}