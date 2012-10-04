package com.netomarin.tablemountain.data;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.netomarin.tablemountain.commons.Constants;
import com.netomarin.tablemountain.provider.RSSProvider.POST;
import com.netomarin.tablemountain.rss.atom.Entry;

import org.json.JSONArray;

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
        
        values.put(POST._ID, 0);
        values.put(POST.POST_ID, entry.getId());
        values.put(POST.FEED_ID, 0);
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
        Entry entry = new Entry();
        
        return entry;
    }
}