package com.marija.diplomski.places.core.domain.model.api.place;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlacesServerResponse {

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions = new ArrayList<>();
    @SerializedName("next_page_token")
    private String nextPageToken;
    @SerializedName("results")
    private List<Result> results = new ArrayList<>();
    @SerializedName("status")
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}