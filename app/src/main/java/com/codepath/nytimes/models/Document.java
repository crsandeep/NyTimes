
package com.codepath.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Document {

    @SerializedName("docs")
    @Expose
    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }
}
