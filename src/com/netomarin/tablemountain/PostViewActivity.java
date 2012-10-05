package com.netomarin.tablemountain;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.netomarin.tablemountain.rss.atom.Entry;

public class PostViewActivity extends Activity {
    
    public static final String PARAM_POST = "POST";
    
    private Entry post;
    
    private PostViewFragment postFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        
        setContentView(R.layout.post_view_activity_layout);
        
        this.post = (Entry) getIntent().getSerializableExtra(PARAM_POST);
        this.postFragment = (PostViewFragment) getFragmentManager().findFragmentById(R.id.postViewActivityFragment);
        postFragment.showPost(post);
    }
}