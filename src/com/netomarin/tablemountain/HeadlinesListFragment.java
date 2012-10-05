
package com.netomarin.tablemountain;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.netomarin.tablemountain.adapter.HeadlinesListAdapter;
import com.netomarin.tablemountain.rss.atom.AtomFeedFetcher;
import com.netomarin.tablemountain.rss.atom.Entry;
import com.netomarin.tablemountain.rss.atom.Feed;

public class HeadlinesListFragment extends ListFragment {

    private static final String KEY_FEED = "feed";
    private Feed feed;
    private HeadlinesListAdapter adapter;
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adapter = new HeadlinesListAdapter(getActivity());
        setListAdapter(adapter);
        
        if (savedInstanceState != null) {
            this.feed = (Feed) savedInstanceState.getSerializable(KEY_FEED);
        }
        
        if (this.feed == null) {
            getRSSFeed();
        } else {
            adapter.setEntries(feed.getEntries());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_FEED, feed);
    }

    private void getRSSFeed() {
        new AsyncTask<Void, Void, Feed>() {

            protected void onPreExecute() {
                progress = ProgressDialog.show(getActivity(), null, "Buscando not’cias...");
            }

            @Override
            protected Feed doInBackground(Void... params) {
                AtomFeedFetcher fetcher = new AtomFeedFetcher(
                        "android-developers.blogspot.com/atom.xml");
                Feed feed = fetcher.fetchFeed();
                return feed;
            }

            protected void onPostExecute(Feed result) {
                if (result != null &&
                        (result.getEntries() != null && result.getEntries().size() > 0)) {
                    feed = result;
                    adapter.setEntries(feed.getEntries());
                    adapter.notifyDataSetChanged();
                }
                progress.dismiss();
            }
        }.execute();
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Entry selected = feed.getEntries().get(position);
        
        Fragment f = getFragmentManager().findFragmentById(R.id.postViewFragment);
        if (f != null && f.isInLayout()) {
            ((PostViewFragment)f).showPost(selected);
        } else {
            Intent i = new Intent(getActivity(), PostViewActivity.class);
            i.putExtra(PostViewActivity.PARAM_POST, selected);
            startActivity(i);
        }
    }
}