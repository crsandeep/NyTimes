package com.codepath.nytimes.network;

import com.codepath.nytimes.models.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ArticleApiInterface {
    @GET("/svc/search/v2/articlesearch.json")
    Call<Response> getArticles(@QueryMap Map<String, String> query);

    @GET("/svc/topstories/v2/politics.json")
    Call<Response> getPopularPolitics(@QueryMap Map<String, String> query);

    @GET("/svc/topstories/v2/national.json")
    Call<Response> getPopularNational(@QueryMap Map<String, String> query);

    @GET("/svc/topstories/v2/sports.json")
    Call<Response> getPopularSports(@QueryMap Map<String, String> query);

    @GET("/svc/topstories/v2/fashion.json")
    Call<Response> getPopularFashion(@QueryMap Map<String, String> query);
}