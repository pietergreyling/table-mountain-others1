
package com.netomarin.tablemountain.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.netomarin.tablemountain.commons.CommonsUtilities;
import com.netomarin.tablemountain.data.FeedDAO;
import com.netomarin.tablemountain.data.PostDAO;
import com.netomarin.tablemountain.provider.RSSProvider;
import com.netomarin.tablemountain.rss.atom.AtomFeedFetcher;
import com.netomarin.tablemountain.rss.atom.Entry;
import com.netomarin.tablemountain.rss.atom.Feed;

import java.util.ArrayList;

public class FeedIntentService extends IntentService {

    public static final String EXTRA_FEED_URL = "FEED_URL";
    public static final String EXTRA_FEED_ID = "FEED_ID";
    public static final String EXTRA_CALLBACK_HANDLER = "CALLBACK_HANDLER";

    public FeedIntentService() {
        super(FeedIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Messenger messenger = (Messenger) intent.getExtras().get(EXTRA_CALLBACK_HANDLER);
        String feedUrl = intent.getStringExtra(EXTRA_FEED_URL);
        int feedId = intent.getIntExtra(EXTRA_FEED_ID, -1);
        Message msg = Message.obtain();
        Feed feed = null;
        boolean fromCache = false;

        if (CommonsUtilities.isOnline(this)) {
            // Downloading feed
            AtomFeedFetcher fetcher = new AtomFeedFetcher(feedUrl);
            feed = fetcher.fetchFeed();
        } else {
            // trying to get from local database, as a cache
            feed = getLocalFeed(feedUrl);
            fromCache = true;
        }

        if (feed != null &&
                (feed.getEntries() != null && feed.getEntries().size() > 0)) {
            if (!fromCache) {
                feed.setUrl(feedUrl);
                saveFeed(feedId, feed);
            }
            msg.arg1 = Activity.RESULT_OK;
            msg.obj = feed;
        } else {
            msg.arg1 = Activity.RESULT_CANCELED;
        }

        try {
            if (messenger != null)
                messenger.send(msg);
        } catch (RemoteException e) {
            Log.e("TableMountain", "Error sending message back");
            e.printStackTrace();
        }
    }

    private Feed getLocalFeed(String feedUrl) {
        Feed feed = null;
        Cursor c = getContentResolver().query(RSSProvider.FEED.CONTENT_URI,
                RSSProvider.FEED.ALL_COLUMNS, RSSProvider.FEED.URL + " = ?", new String[] {
                    feedUrl
                }, null);

        if (c.moveToFirst()) {
            feed = FeedDAO.fromCursor(c);
            feed.setEntries(getFeedEntries(feed.get_id()));
        }

        return feed;
    }

    private ArrayList<Entry> getFeedEntries(long get_id) {
        ArrayList<Entry> entries = null;

        Cursor c = getContentResolver().query(
                ContentUris.withAppendedId(RSSProvider.POST_LIST.CONTENT_URI, get_id),
                RSSProvider.POST.ALL_COLUMNS, null, null, null);

        if (c.moveToFirst()) {
            entries = new ArrayList<Entry>();
            do {
                entries.add(PostDAO.fromCursor(c));
            } while (c.moveToNext());
        }

        return entries;
    }

    private Uri saveFeed(int feedId, Feed feed) {
        Uri feedUri = null;

        ContentValues feedCV = FeedDAO.toContentValues(feed);
        if (feedId != -1) {
            // updating feed
            String selectionClause = RSSProvider.FEED._ID + " = ?";
            String[] selectionArgs = {
                    Integer.toString(feedId)
            };

            getContentResolver().update(RSSProvider.FEED.CONTENT_URI, feedCV, selectionClause,
                    selectionArgs);
            feedUri = Uri.parse("content://" + RSSProvider.AUTHORITY + "/feed/" + feedId);
        } else {
            feedUri = getContentResolver().insert(RSSProvider.FEED.CONTENT_URI, feedCV);
            if (feedUri != null) {
                String uriStr = feedUri.toString();
                feedId = Integer.parseInt(uriStr.substring(uriStr.lastIndexOf("/") + 1));
            }
        }

        //TODO delete previous posts
        ContentValues postCV = null;
        for (Entry entry : feed.getEntries()) {
            postCV = PostDAO.toContentValues(entry);
            postCV.put(RSSProvider.POST.FEED_ID, feedId);
            getContentResolver().insert(RSSProvider.POST.CONTENT_URI, postCV);
        }

        return feedUri;
    }
}