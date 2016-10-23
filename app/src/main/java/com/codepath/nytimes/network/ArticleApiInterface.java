package com.codepath.nytimes.network;

import com.codepath.nytimes.models.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ArticleApiInterface {
    @GET("/svc/search/v2/articlesearch.json")
    public Call<Response> getArticles(@QueryMap Map<String, String> query);
}
