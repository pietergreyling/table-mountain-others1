package com.netomarin.tablemountain.rss.atom;

import java.io.Serializable;

public class Author implements Serializable {

    private static final long serialVersionUID = -9006349980898721852L;
    
    private String name;
    private String url;
    private String email;
    private String image;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }    
}