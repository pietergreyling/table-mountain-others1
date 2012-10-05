package com.netomarin.tablemountain;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.netomarin.tablemountain.rss.atom.Entry;

import java.text.DateFormat;

public class PostViewFragment extends Fragment {
    
    private Entry post;
    
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView publishedTextView;
    private WebView postWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_view_layout, container, false);
        
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
    }
}