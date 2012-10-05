
package com.netomarin.tablemountain;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TableMountainReaderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_mountain_reader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_table_mountain_reader, menu);
        return true;
    }
}