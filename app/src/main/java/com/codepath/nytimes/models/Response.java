
package com.codepath.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("response")
    @Expose
    private Document document;

    public Document getDocument() {
        return document;
    }
}
