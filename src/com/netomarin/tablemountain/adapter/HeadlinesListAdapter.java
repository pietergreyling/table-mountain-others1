
package com.netomarin.tablemountain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netomarin.tablemountain.R;
import com.netomarin.tablemountain.rss.atom.Entry;

import java.util.ArrayList;

public class HeadlinesListAdapter extends BaseAdapter {

    private ArrayList<Entry> entries;
    private LayoutInflater inflater;

    private class ViewHolder {
        TextView headlineTitleTextView;
        TextView headlineAuthorTextView;
    }

    public HeadlinesListAdapter(Context context) {
        this(context, new ArrayList<Entry>());
    }

    public HeadlinesListAdapter(Context context, ArrayList<Entry> entries) {
        if (entries == null)
            throw new IllegalArgumentException("Entries can't be null!");
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.entries = entries;
    }
    
    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public int getCount() {
        return entries.size();
    }

    public Object getItem(int position) {
        return entries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Entry entry = (Entry) getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.headline_item_layout, null);
            holder = new ViewHolder();
            holder.headlineTitleTextView = (TextView) convertView
                    .findViewById(R.id.headlineTitleTextView);
            holder.headlineAuthorTextView = (TextView) convertView
                    .findViewById(R.id.headlineAuthorTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineTitleTextView.setText(entry.getTitle());
        if (entry.getAuthor() != null) {
            holder.headlineAuthorTextView.setText("by " + entry.getAuthor().getName());
        }

        return convertView;
    }
}
