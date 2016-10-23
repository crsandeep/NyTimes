
package com.codepath.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Article {

    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedium> multimedia = new ArrayList<Multimedium>();
    @SerializedName("headline")
    @Expose
    private Headline headline;

    public String getWebUrl() {
        return webUrl;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

}
