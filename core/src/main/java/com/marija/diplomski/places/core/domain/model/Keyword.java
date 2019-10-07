package com.marija.diplomski.places.core.domain.model;

import java.io.Serializable;

public class Keyword implements Serializable {

    private Integer id;
    private String title;
    private String type;
    private String icon;

    public Keyword(){}

    public Keyword(Integer id, String title, String type, String icon) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.icon = icon;
    }

    public Keyword(String title, String icon) {
        this(null, title, "", icon);
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon(){
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
