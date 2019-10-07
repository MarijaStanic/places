package com.marija.diplomski.places.core.domain.model;

import java.io.Serializable;

public class Folder implements Serializable {

    private Integer id;
    private String name;
    boolean isSelected;

    public Folder(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Folder(String name){
        this(null, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
