
package com.netomarin.tablemountain;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.netomarin.tablemountain.rss.atom.Entry;

import java.text.DateFormat;

public class PostViewFragment extends Fragment {

    private Entry post;

    private ShareActionProvider shareActionProvider;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView publishedTextView;
    private WebView postWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_view_layout, container, false);
        setHasOptionsMenu(true);

        titleTextView = (TextView) view.findViewById(R.id.postTitleTextView);
        authorTextView = (TextView) view.findViewById(R.id.authorTextView);
        publishedTextView = (TextView) view.findViewById(R.id.publishedDateTextView);
        postWebView = (WebView) view.findViewById(R.id.postWebView);
        postWebView.getSettings().setJavaScriptEnabled(true);
        postWebView.getSettings().setBuiltInZoomControls(true);

        return view;
    }

    public void showPost(Entry entry) {
        this.post = entry;

        titleTextView.setText(post.getTitle());
        authorTextView.setText(post.getAuthor().getName());
        publishedTextView.setText(DateFormat.getDateTimeInstance().format(post.getPublished()));
        postWebView.loadDataWithBaseURL(null, post.getContent(), "text/html", "UTF-8", null);
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createPostShareIntent());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        shareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        if (post != null) {
            shareActionProvider.setShareIntent(createPostShareIntent());
        }
    }

    private Intent createPostShareIntent() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_TITLE, post.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getAlternateLink());

        return shareIntent;
    }
}
