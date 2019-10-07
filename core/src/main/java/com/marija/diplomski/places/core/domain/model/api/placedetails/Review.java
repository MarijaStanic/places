package com.marija.diplomski.places.core.domain.model.api.placedetails;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author_name")
    private String authorName;

    private Integer rating;
    private String text;
    private Integer time;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
