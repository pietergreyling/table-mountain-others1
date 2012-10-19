
package com.netomarin.tablemountain.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.netomarin.tablemountain.data.FeedDAO;
import com.netomarin.tablemountain.data.PostDAO;
import com.netomarin.tablemountain.data.RSSOpenHelper;

public class RSSProvider extends ContentProvider {

    public static final String AUTHORITY = "com.netomarin.tablemountain.provider.rss";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private final static int FEED_PROVIDER_URI = 0;
    private final static int FEED_ITEM_PROVIDER_URI = 1;
    private final static int POST_PROVIDER_URI = 2;
    private final static int POST_ITEM_PROVIDER_URI = 3;
    private final static int POST_LIST_PROVIDER_URI = 4;

    private final static UriMatcher URI_MATCHER;

    private RSSOpenHelper helper;
    private SQLiteDatabase readableDB;
    private SQLiteDatabase writeableDB;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "feed", FEED_PROVIDER_URI);
        URI_MATCHER.addURI(AUTHORITY, "feed/#", FEED_ITEM_PROVIDER_URI);
        URI_MATCHER.addURI(AUTHORITY, "post", POST_PROVIDER_URI);
        URI_MATCHER.addURI(AUTHORITY, "post/#", POST_ITEM_PROVIDER_URI);
        URI_MATCHER.addURI(AUTHORITY, "posts/#", POST_LIST_PROVIDER_URI);
    }

    public static final class POST_LIST implements BaseColumns {
        public static final String POST_LIST = "POST_LIST";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/posts");
    }

    public static final class POST implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/post");

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
        public static final String[] ALL_COLUMNS = {
                _ID, POST_ID, FEED_ID, PUBLISHED, UPDATED,
                EDITED, CATEGORIES, TITLE, CONTENT, AUTHOR, CONTRIBUTORS
        };
    }

    public static final class FEED implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/feed");
        public static final String _ID = "_ID";
        public static final String FEED_ID = "FEED_ID";
        public static final String URL = "URL";
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
        public static final String[] ALL_COLUMNS = {
                _ID, FEED_ID, URL, UPDATED, CATEGORIES, TITLE,
                SUBTITLE, ALT_LINK, NEXT_LINK, AUTHOR, TOTAL_RESULTS, START_INDEX, ITEMS_PER_PAGE
        };
    }

    @Override
    public boolean onCreate() {
        helper = new RSSOpenHelper(getContext());

        if (helper != null) {
            readableDB = helper.getReadableDatabase();
            writeableDB = helper.getWritableDatabase();
        }

        return helper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case FEED_PROVIDER_URI:
                return selectFeed(projection, selection, selectionArgs);
            case POST_PROVIDER_URI:
                String postUriString = uri.toString();
                long postId = Long
                        .parseLong(postUriString.substring(postUriString.lastIndexOf("/") + 1));
                return selectPost(postId);
            case POST_LIST_PROVIDER_URI:
                String uriString = uri.toString();
                long feedId = Long.parseLong(uriString.substring(uriString.lastIndexOf("/") + 1));
                return selectPostsFromFeed(feedId);
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
    }

    private Cursor selectPost(long postId) {
        Cursor c = readableDB.query(PostDAO.POST_TABLE, POST.ALL_COLUMNS, POST._ID + "= ?",
                new String[] {
                    Long.toString(postId)
                }, null, null, null);

        return c;
    }

    private Cursor selectPostsFromFeed(long feedId) {
        Cursor c = readableDB.query(PostDAO.POST_TABLE, POST.ALL_COLUMNS, POST.FEED_ID + "= ?",
                new String[] {
                    Long.toString(feedId)
                }, null, null, null);

        return c;
    }

    private Cursor selectFeed(String[] projection, String selection, String[] selectionArgs) {
        Cursor c = readableDB.query(FeedDAO.FEED_TABLE, projection, selection, selectionArgs, null,
                null,
                null);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case FEED_PROVIDER_URI:
                return deleteFeed(selection, selectionArgs);
            case POST_PROVIDER_URI:
                String postUriString = uri.toString();
                long postId = Long
                        .parseLong(postUriString.substring(postUriString.lastIndexOf("/") + 1));
                return deletePost(postId);
            case POST_LIST_PROVIDER_URI:
                String uriString = uri.toString();
                long feedId = Long.parseLong(uriString.substring(uriString.lastIndexOf("/") + 1));
                return deletePostsFromFeed(feedId);
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
    }

    private int deletePostsFromFeed(long feedId) {
        int rows = writeableDB.delete(PostDAO.POST_TABLE, POST.FEED_ID + " = ?", new String[] {
            Long.toString(feedId)
        });
        return rows;
    }

    private int deletePost(long postId) {
        int rows = writeableDB.delete(PostDAO.POST_TABLE, POST._ID + " = ?", new String[] {
            Long.toString(postId)
        });

        return rows;
    }

    private int deleteFeed(String selection, String[] selectionArgs) {
        int rows = writeableDB.delete(FeedDAO.FEED_TABLE, selection, selectionArgs);
        return rows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri insertedRow = null;

        switch (URI_MATCHER.match(uri)) {
            case FEED_PROVIDER_URI:
                insertedRow = insertFeed(values);
                break;
            case POST_PROVIDER_URI:
                insertedRow = insertPost(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        return insertedRow;
    }
    
    private Uri insertPost(ContentValues values) {
        if (!values.containsKey(POST.FEED_ID) ||
                !values.containsKey(POST.TITLE) ||
                !values.containsKey(POST.CONTENT)) {
            throw new IllegalArgumentException("Missing required field");
        }

        long newId = writeableDB.insert(PostDAO.POST_TABLE, null, values);

        return Uri.parse("content://" + AUTHORITY + "/post/" + newId);
    }

    private Uri insertFeed(ContentValues values) throws IllegalArgumentException {
        if (!values.containsKey(FEED.TITLE)) {
            throw new IllegalArgumentException("Missing required field: title");
        }

        long newId = writeableDB.insert(FeedDAO.FEED_TABLE, "", values);

        return Uri.parse("content://" + AUTHORITY + "/feed/" + newId);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case FEED_ITEM_PROVIDER_URI:
                String feedUriString = uri.toString();
                long feedId = Long
                        .parseLong(feedUriString.substring(feedUriString.lastIndexOf("/") + 1));
                return updateFeed(feedId, values);
            case POST_ITEM_PROVIDER_URI:
                String postUriString = uri.toString();
                long postId = Long
                        .parseLong(postUriString.substring(postUriString.lastIndexOf("/") + 1));
                return updatePost(postId, values);
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
    }

    private int updateFeed(long feedId, ContentValues values) {
        int rows = writeableDB.update(FeedDAO.FEED_TABLE, values, FEED._ID + " = ?", new String[] {
            Long.toString(feedId)
        });

        return rows;
    }

    private int updatePost(long postId, ContentValues values) {
        int rows = writeableDB.update(PostDAO.POST_TABLE, values, POST._ID + " = ?", new String[] {
            Long.toString(postId)
        });

        return rows;
    }
}