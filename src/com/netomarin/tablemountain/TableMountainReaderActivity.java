
package com.netomarin.tablemountain;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.netomarin.tablemountain.rss.atom.AtomFeedFetcher;
import com.netomarin.tablemountain.rss.atom.Feed;

public class TableMountainReaderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_mountain_reader);

        getRSSFeed();
    }

    private void getRSSFeed() {
        new AsyncTask<Void, Void, Feed>() {

            @Override
            protected Feed doInBackground(Void... params) {
                AtomFeedFetcher fetcher = new AtomFeedFetcher("android-developers.blogspot.com/atom.xml");
                Feed feed = fetcher.fetchFeed();
                return feed;
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_table_mountain_reader, menu);
        return true;
    }
}
