package com.netomarin.tablemountain.rss.atom;

import java.util.ArrayList;
import java.util.Date;

public class Feed {

    private String id;
    private Date updated;
    private ArrayList<String> categories;
    private String title;
    private String subtitle;
    private String alternateLink;
    private String nextLink;
    private Author author;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private ArrayList<Entry> entries;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Date getUpdated() {
        return updated;
    }
    
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public ArrayList<String> getCategories() {
        return categories;
    }
    
    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
    
    public void addCategory(String category) {
        if (categories == null)
            categories = new ArrayList<String>();
        
        this.categories.add(category);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    
    public String getAlternateLink() {
        return alternateLink;
    }
    
    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }
    
    public String getNextLink() {
        return nextLink;
    }
    
    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public int getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    
    public int getItemsPerPage() {
        return itemsPerPage;
    }
    
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
    
    public ArrayList<Entry> getEntries() {
        return entries;
    }
    
    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }
    
    public void addEntry(Entry entry) {
        if (entries == null) {
            this.entries = new ArrayList<Entry>();
        }
        
        this.entries.add(entry);
    }
}