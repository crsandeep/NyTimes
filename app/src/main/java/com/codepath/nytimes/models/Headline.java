
package com.codepath.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Headline {

    @SerializedName("main")
    @Expose
    private String main;

    public String getMain() {
        return main;
    }
}