package com.codepath.nytimes.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PopularArticle {
    public String webUrl;

    public String imageUrl;

    public String title;

    public static List<PopularArticle> fromJson(JSONArray movieJsonResults) {
        ArrayList<PopularArticle> result = new ArrayList<>();
        for(int i = 0; i < movieJsonResults.length(); i++ ) {
            PopularArticle p = new PopularArticle();
            try {
                p.title = movieJsonResults.getJSONObject(i).getString("title");
                p.webUrl = movieJsonResults.getJSONObject(i).getString("url");
                JSONArray j = movieJsonResults.getJSONObject(i).getJSONArray("multimedia");
                if(j !=null && j.length() > 0) {
                    p.imageUrl = j.getJSONObject(0).getString("url");
                }
                result.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
