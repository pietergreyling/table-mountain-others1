package com.netomarin.tablemountain.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RSSOpenHelper extends SQLiteOpenHelper {
    
    private static final int RSS_DB_VERSION = 1;
    public static final String RSS_DB_NAME = "RSS";

    public RSSOpenHelper(Context context) {
        super(context, RSS_DB_NAME, null, RSS_DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+FeedDAO.FEED_TABLE +" ( "+
                FeedDAO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                FeedDAO.FEED_ID + " TEXT, "+
                FeedDAO.URL + " TEXT, "+
                FeedDAO.UPDATED + " TEXT, "+
                FeedDAO.CATEGORIES + " TEXT, "+
                FeedDAO.TITLE + " TEXT, "+
                FeedDAO.SUBTITLE + " TEXT, "+
                FeedDAO.ALT_LINK + " TEXT, "+
                FeedDAO.NEXT_LINK + " TEXT, "+
                FeedDAO.AUTHOR + " TEXT, "+
                FeedDAO.TOTAL_RESULTS + " NUMERIC, "+
                FeedDAO.START_INDEX + " NUMERIC, "+
                FeedDAO.ITEMS_PER_PAGE + " NUMERIC )");
        
        db.execSQL("CREATE TABLE " + PostDAO.POST_TABLE + " ( "+
                PostDAO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                PostDAO.POST_ID + " TEXT, "+
                PostDAO.FEED_ID + " NUMERIC, "+
                PostDAO.PUBLISHED + " TEXT, "+
                PostDAO.UPDATED + " TEXT, "+
                PostDAO.EDITED + " TEXT, "+
                PostDAO.CATEGORIES + " TEXT, "+
                PostDAO.TITLE + " TEXT, "+
                PostDAO.CONTENT + " TEXT, "+
                PostDAO.AUTHOR + " TEXT, "+
                PostDAO.CONTRIBUTORS + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * First version. This method will never be called.
         */
    }

}
