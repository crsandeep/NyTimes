package com.codepath.nytimes.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.codepath.nytimes.R;
import com.codepath.nytimes.adapters.HorizontalAdapter;
import com.codepath.nytimes.adapters.ItemClickSupport;
import com.codepath.nytimes.databinding.ActivityPopularBinding;
import com.codepath.nytimes.models.PopularArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PopularActivity extends AppCompatActivity {

    private HorizontalAdapter horizontalPoliticsAdapter, horizontalNationalAdapter, horizontalSportsAdapter, horizontalFashionAdapter;

    LinearLayout linearLayout;

    List<PopularArticle> politicsList = new ArrayList<>();
    List<PopularArticle> nationalList = new ArrayList<>();
    List<PopularArticle> sportsList = new ArrayList<>();
    List<PopularArticle> fashionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPopularBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_popular);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.nytimes);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        linearLayout = binding.popularLinearLayout;

        if (!isNetworkAvailable(this)) {
            Snackbar.make(linearLayout, R.string.not_connected, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                    v -> {
                        this.recreate();
                    }).show();
        }

        horizontalPoliticsAdapter=new HorizontalAdapter(this, politicsList);
        horizontalNationalAdapter=new HorizontalAdapter(this, nationalList);
        horizontalSportsAdapter=new HorizontalAdapter(this, sportsList);
        horizontalFashionAdapter=new HorizontalAdapter(this, fashionList);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(PopularActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(PopularActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager3
                = new LinearLayoutManager(PopularActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager4
                = new LinearLayoutManager(PopularActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.horizontalRecyclerViewPolitics.setLayoutManager(horizontalLayoutManager);
        binding.horizontalRecyclerViewPolitics.setAdapter(horizontalPoliticsAdapter);

        binding.horizontalRecyclerViewNational.setLayoutManager(horizontalLayoutManager2);
        binding.horizontalRecyclerViewNational.setAdapter(horizontalNationalAdapter);

        binding.horizontalRecyclerViewSports.setLayoutManager(horizontalLayoutManager3);
        binding.horizontalRecyclerViewSports.setAdapter(horizontalSportsAdapter);

        binding.horizontalRecyclerViewFashion.setLayoutManager(horizontalLayoutManager4);
        binding.horizontalRecyclerViewFashion.setAdapter(horizontalFashionAdapter);



        ItemClickSupport.addTo(binding.horizontalRecyclerViewPolitics).setOnItemClickListener(
                (RecyclerView recyclerView, int position, View v) -> {
                    PopularArticle article = politicsList.get(position);
                    String url = article.webUrl;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, article.webUrl);

                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(PopularActivity.this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(PopularActivity.this, R.color.colorPrimary));
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(PopularActivity.this, Uri.parse(url));
                });

        ItemClickSupport.addTo(binding.horizontalRecyclerViewFashion).setOnItemClickListener(
                (RecyclerView recyclerView, int position, View v) -> {
                    PopularArticle article = fashionList.get(position);
                    String url = article.webUrl;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, article.webUrl);

                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(PopularActivity.this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(PopularActivity.this, R.color.colorPrimary));
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(PopularActivity.this, Uri.parse(url));
                });

        ItemClickSupport.addTo(binding.horizontalRecyclerViewNational).setOnItemClickListener(
                (RecyclerView recyclerView, int position, View v) -> {
                    PopularArticle article = nationalList.get(position);
                    String url = article.webUrl;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, article.webUrl);

                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(PopularActivity.this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(PopularActivity.this, R.color.colorPrimary));
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(PopularActivity.this, Uri.parse(url));
                });

        ItemClickSupport.addTo(binding.horizontalRecyclerViewSports).setOnItemClickListener(
                (RecyclerView recyclerView, int position, View v) -> {
                    PopularArticle article = sportsList.get(position);
                    String url = article.webUrl;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, article.webUrl);

                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(PopularActivity.this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(PopularActivity.this, R.color.colorPrimary));
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(PopularActivity.this, Uri.parse(url));
                });




        OkHttpClient client = new OkHttpClient();

        String url = "https://api.nytimes.com/svc/topstories/v2/politics.json?api-key=bfa504d8afec47ba9757b3dab9201ddd";

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread( () -> {
                        try {
                            JSONObject responseJSON = new JSONObject(responseData);
                            JSONArray jsonResults = responseJSON.getJSONArray("results");
                            politicsList.addAll(PopularArticle.fromJson(jsonResults));
                            horizontalPoliticsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                });
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });

        url = "https://api.nytimes.com/svc/topstories/v2/national.json?api-key=bfa504d8afec47ba9757b3dab9201ddd";

        request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                        try {
                            JSONObject responseJSON = new JSONObject(responseData);
                            JSONArray jsonResults = responseJSON.getJSONArray("results");
                            nationalList.addAll(PopularArticle.fromJson(jsonResults));
                            horizontalNationalAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                });
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });

        url = "https://api.nytimes.com/svc/topstories/v2/sports.json?api-key=bfa504d8afec47ba9757b3dab9201ddd";

        request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                        try {
                            JSONObject responseJSON = new JSONObject(responseData);
                            JSONArray jsonResults = responseJSON.getJSONArray("results");
                            sportsList.addAll(PopularArticle.fromJson(jsonResults));
                            horizontalSportsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                });
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });

        url = "https://api.nytimes.com/svc/topstories/v2/fashion.json?api-key=bfa504d8afec47ba9757b3dab9201ddd";

        request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(() -> {
                        try {
                            JSONObject responseJSON = new JSONObject(responseData);
                            JSONArray jsonResults = responseJSON.getJSONArray("results");
                            fashionList.addAll(PopularArticle.fromJson(jsonResults));
                            horizontalFashionAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                });
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_popular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Intent intent = new Intent(PopularActivity.this, ArticleListActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
