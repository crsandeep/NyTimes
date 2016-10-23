
package com.codepath.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Multimedium {

    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return "https://www.nytimes.com/" + url;
    }
}
