
package com.netomarin.tablemountain;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.netomarin.tablemountain.adapter.HeadlinesListAdapter;
import com.netomarin.tablemountain.rss.atom.Entry;
import com.netomarin.tablemountain.rss.atom.Feed;
import com.netomarin.tablemountain.service.FeedIntentService;

public class HeadlinesListFragment extends ListFragment {

    private static final int REQ_READ_ACTIVITY = 100;

    private static final String KEY_FEED = "feed";
    private static final String KEY_POST_SELECTED = "post_selected";
    private Feed feed;
    private HeadlinesListAdapter adapter;
    private ProgressDialog progress;
    private int postSelected;
    private FeedHandler feedHandler;
    
    private ImageView refreshImageView;
    private Animation refreshAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.feedHandler = new FeedHandler(this);
        this.postSelected = -1;
        this.adapter = new HeadlinesListAdapter(getActivity());
        setListAdapter(adapter);
        setHasOptionsMenu(true);
        refreshImageView = new ImageView(getActivity());
        refreshImageView.setImageResource(R.drawable.ic_action_refresh);
        refreshImageView.setOnClickListener(new OnClickListener() {            
            public void onClick(View v) {
                refreshHeadlines();
            }
        });
        
        if (savedInstanceState != null) {
            this.feed = (Feed) savedInstanceState.getSerializable(KEY_FEED);
            this.postSelected = savedInstanceState.getInt(KEY_POST_SELECTED);
        }
    }

    private void refreshHeadlines() {
        refreshAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        refreshAnimation.setRepeatCount(Animation.INFINITE);        
        refreshImageView.startAnimation(refreshAnimation);
        getRSSFeed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_READ_ACTIVITY) {
            Fragment f = getFragmentManager().findFragmentById(R.id.postViewFragment);
            if (f == null || !f.isInLayout()) {
                postSelected = -1;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.feed == null) {
            progress = ProgressDialog.show(getActivity(), null, getString(R.string.txt_getting_news));
            getRSSFeed();
        } else {
            adapter.setEntries(feed.getEntries());
            adapter.notifyDataSetChanged();

            if (postSelected >= 0) {
                showPost(feed.getEntries().get(postSelected), false);
            } else {
                showPost(feed.getEntries().get(0), true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_FEED, feed);
        outState.putInt(KEY_POST_SELECTED, postSelected);
    }

    private void getRSSFeed() {
        Intent feedIntent = new Intent(getActivity(), FeedIntentService.class);
        feedIntent.putExtra(FeedIntentService.EXTRA_FEED_URL,
                "android-developers.blogspot.com/atom.xml");
        feedIntent.putExtra(FeedIntentService.EXTRA_CALLBACK_HANDLER,
                new Messenger(feedHandler));

        getActivity().startService(feedIntent);
    }

    public void onFeedReceived(Feed feed) {
        if (feed != null &&
                (feed.getEntries() != null && feed.getEntries().size() > 0)) {
            this.feed = feed;
            adapter.setEntries(feed.getEntries());
            adapter.notifyDataSetChanged();
            showPost(feed.getEntries().get(0), true);
        }

        if (refreshAnimation != null)
            refreshAnimation.cancel();
        if (progress != null)
            progress.dismiss();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Entry selected = feed.getEntries().get(position);
        postSelected = position;
        showPost(selected, false);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.menu_refresh);
        item.setActionView(this.refreshImageView);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            
            refreshImageView.startAnimation(rotation);
//            item.setActionView(iv);
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void showPost(Entry selected, boolean onlyHorizontal) {
        Fragment f = getFragmentManager().findFragmentById(R.id.postViewFragment);
        if (f != null && f.isInLayout()) {
            ((PostViewFragment) f).showPost(selected);
        } else if (!onlyHorizontal) {
            Intent i = new Intent(getActivity(), PostViewActivity.class);
            i.putExtra(PostViewActivity.PARAM_POST, selected);
            startActivityForResult(i, REQ_READ_ACTIVITY);
        }
    }

    public void feedError() {
        if (refreshAnimation != null)
            refreshAnimation.cancel();
        
        if (progress != null)
            progress.dismiss();

        Toast.makeText(getActivity(), getString(R.string.error_no_connection), Toast.LENGTH_SHORT)
                .show();
    }

    private static class FeedHandler extends Handler {
        private HeadlinesListFragment list;

        public FeedHandler(HeadlinesListFragment list) {
            this.list = list;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == Activity.RESULT_OK) {
                list.onFeedReceived((Feed) msg.obj);
            } else {
                list.feedError();
            }
        }
    }
}
