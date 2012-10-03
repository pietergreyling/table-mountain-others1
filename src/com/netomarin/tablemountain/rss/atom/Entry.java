package com.netomarin.tablemountain.rss.atom;

import java.util.ArrayList;
import java.util.Date;

public class Entry {

    private String id;
    private Date published;
    private Date updated;
    private Date edited;
    private ArrayList<String> categories;
    private String title;
    private String content;
    private Author author;
    private ArrayList<String> contributors;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Date getPublished() {
        return published;
    }
    
    public void setPublished(Date published) {
        this.published = published;
    }
    
    public Date getUpdated() {
        return updated;
    }
    
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Date getEdited() {
        return edited;
    }
    
    public void setEdited(Date edited) {
        this.edited = edited;
    }
    
    public ArrayList<String> getCategories() {
        return categories;
    }
    
    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
    
    public void addCategory(String category) {
        if (this.categories == null)
            this.categories = new ArrayList<String>();
        this.categories.add(category);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }
    
    public void addContributor(String contributor) {
        if (this.contributors == null)
            this.contributors = new ArrayList<String>();
        contributors.add(contributor);
    }
}