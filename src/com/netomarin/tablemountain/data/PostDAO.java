
package com.netomarin.tablemountain.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.netomarin.tablemountain.commons.Constants;
import com.netomarin.tablemountain.provider.RSSProvider.POST;
import com.netomarin.tablemountain.rss.atom.Author;
import com.netomarin.tablemountain.rss.atom.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PostDAO {

    public static final String POST_TABLE = "POST";

    public static final String _ID = "_ID";
    public static final String POST_ID = "POST_ID";
    public static final String FEED_ID = "FEED_ID";
    public static final String PUBLISHED = "PUBLISHED";
    public static final String UPDATED = "UPDATED";
    public static final String EDITED = "EDITED";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String AUTHOR = "AUTHOR";
    public static final String CONTRIBUTORS = "CONTRIBUTORS";

    public static ContentValues toContentValues(Entry entry) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATTER_PATTERN);
        ContentValues values = new ContentValues();

        if (entry.get_id() > 0)
            values.put(POST._ID, entry.get_id());
        
        values.put(POST.POST_ID, entry.getId());
        
        if (entry.getFeedId() > 0)
            values.put(POST.FEED_ID, entry.getFeedId());
        
        if (entry.getPublished() != null)
            values.put(POST.PUBLISHED, formatter.format(entry.getPublished()));

        if (entry.getUpdated() != null)
            values.put(POST.UPDATED, formatter.format(entry.getUpdated()));

        if (entry.getEdited() != null)
            values.put(POST.EDITED, formatter.format(entry.getEdited()));

        if (entry.getCategories() != null && entry.getCategories().size() > 0)
            values.put(POST.CATEGORIES, new JSONArray(entry.getCategories()).toString());

        values.put(POST.TITLE, entry.getTitle());
        values.put(POST.CONTENT, entry.getContent());

        if (entry.getAuthor() != null) {
            Gson gson = new Gson();
            values.put(POST.AUTHOR, gson.toJson(entry.getAuthor()));
        }

        if (entry.getContributors() != null && entry.getContributors().size() > 0)
            values.put(POST.CONTRIBUTORS, new JSONArray(entry.getContributors()).toString());

        return values;
    }

    public static Entry fromContentValues(ContentValues values) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATTER_PATTERN);
        Entry entry = new Entry();

        entry.setId(values.getAsString(POST.POST_ID));
        entry.set_id(values.getAsLong(POST._ID));
        entry.setFeedId(values.getAsLong(POST.FEED_ID));

        try {
            if (values.containsKey(POST.PUBLISHED))
                entry.setPublished(formatter.parse(values.getAsString(POST.PUBLISHED)));

            if (values.containsKey(POST.UPDATED))
                entry.setUpdated(formatter.parse(values.getAsString(POST.UPDATED)));

            if (values.containsKey(POST.EDITED))
                entry.setEdited(formatter.parse(values.getAsString(POST.EDITED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (values.containsKey(POST.CATEGORIES)) {
            try {
                JSONArray jsonArray = new JSONArray(values.getAsString(POST.CATEGORIES));
                for (int i = 0; i < jsonArray.length(); i++) {
                    entry.addCategory(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        entry.setTitle(values.getAsString(POST.TITLE));
        entry.setContent(values.getAsString(POST.CONTENT));

        if (values.containsKey(POST.AUTHOR)) {
            Gson gson = new Gson();
            entry.setAuthor(gson.fromJson(values.getAsString(POST.AUTHOR), Author.class));
        }

        if (values.containsKey(POST.CONTRIBUTORS)) {
            try {
                JSONArray jsonArray = new JSONArray(values.getAsString(POST.CONTRIBUTORS));
                for (int i = 0; i < jsonArray.length(); i++) {
                    entry.addContributor(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return entry;
    }
    
    public static Entry fromCursor(Cursor c) {
        Entry entry = new Entry();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATTER_PATTERN);
        
        entry.setId(c.getString(c.getColumnIndex(POST.POST_ID)));
        entry.set_id(c.getLong(c.getColumnIndex(POST._ID)));
        entry.setFeedId(c.getLong(c.getColumnIndex(POST.FEED_ID)));

        try {
            String dateAux = c.getString(c.getColumnIndex(POST.PUBLISHED));
            if (dateAux != null)
                entry.setPublished(formatter.parse(dateAux));

            dateAux = c.getString(c.getColumnIndex(POST.UPDATED));
            if (dateAux != null)
                entry.setUpdated(formatter.parse(dateAux));

            dateAux = c.getString(c.getColumnIndex(POST.EDITED));
            if (dateAux != null)
                entry.setEdited(formatter.parse(dateAux));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String stringJSON = c.getString(c.getColumnIndex(POST.CATEGORIES));
        if (stringJSON != null) {
            try {
                JSONArray jsonArray = new JSONArray(stringJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    entry.addCategory(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        entry.setTitle(c.getString(c.getColumnIndex(POST.TITLE)));
        entry.setContent(c.getString(c.getColumnIndex(POST.CONTENT)));

        stringJSON = c.getString(c.getColumnIndex(POST.AUTHOR));
        if (stringJSON != null) {
            Gson gson = new Gson();
            entry.setAuthor(gson.fromJson(stringJSON, Author.class));
        }

        stringJSON = c.getString(c.getColumnIndex(POST.CONTRIBUTORS));
        if (stringJSON != null) {
            try {
                JSONArray jsonArray = new JSONArray(stringJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    entry.addContributor(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        return entry;
    }
}