package com.codepath.nytimes.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.codepath.nytimes.R;
import com.codepath.nytimes.Utils.Constants;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimes.adapters.ItemClickSupport;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.models.Response;
import com.codepath.nytimes.network.ArticleApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleListActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private ArticleAdapter articleAdapter;
    private List<Article> articleList;

    @BindView(R.id.rvList) RecyclerView rvList;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private EditText etDateRange;

    private SharedPreferences filterPreferences;
    private SharedPreferences.Editor editor;

    private static Integer PAGE_NUMBER = 0;
    private static String QUERY = "";
    private boolean isArtsChecked = false;
    private boolean isMoviesChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.nytimes);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleList);
        rvList.setAdapter(articleAdapter);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(mLayoutManager);

        ItemClickSupport.addTo(rvList).setOnItemClickListener(
                (RecyclerView recyclerView, int position, View v) -> {
                        Article article = articleList.get(position);
                        String url = article.getWebUrl();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

                        int requestCode = 100;

                        PendingIntent pendingIntent = PendingIntent.getActivity(ArticleListActivity.this,
                                requestCode,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);


                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(ArticleListActivity.this, R.color.colorPrimary));
                        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(ArticleListActivity.this, Uri.parse(url));
                });

        filterPreferences = getSharedPreferences("filter_settings", Context.MODE_PRIVATE);
        editor = filterPreferences.edit();

        if (!isNetworkAvailable(this)) {
            Snackbar.make(findViewById(R.id.swipeContainer), R.string.not_connected, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                    v -> {
                        this.recreate();
                    }).show();
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener((StaggeredGridLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                PAGE_NUMBER++;
                fetchArticles(QUERY);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUMBER = 0;
                fetchArticles(QUERY);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search articles..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PAGE_NUMBER = 0;
                QUERY = query;
                fetchArticles(query);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                showFilterDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFilterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        dialogBuilder.setView(dialogView);

        CheckBox checkArts = (CheckBox) dialogView.findViewById(R.id.checkBoxArts);
        checkArts.setChecked(filterPreferences.getBoolean("isArtsChecked", false));

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.oldNewSpinner);
        etDateRange = (EditText) dialogView.findViewById(R.id.etDateRange);

        dialogBuilder.setTitle("Filter");
        dialogBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                isArtsChecked = checkArts.isChecked();
                editor.putBoolean("isArtsChecked", isArtsChecked);
                editor.apply();
                Log.d(Constants.TAG, "showFilterDialog: "+isArtsChecked);

                CheckBox checkMovies = (CheckBox) dialogView.findViewById(R.id.checkBoxArts);
                isMoviesChecked = checkMovies.isChecked();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        etDateRange = (EditText) b.findViewById(R.id.etDateRange);

        etDateRange.setOnClickListener(v -> {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ArticleListActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            });
    }

    private void fetchArticles(final String query) {

        if (PAGE_NUMBER == 0) {
            articleList.clear();
            articleAdapter.notifyDataSetChanged();
        }

        if(TextUtils.isEmpty(query)) {
            swipeContainer.setRefreshing(false);
            return;
        }

        OkHttpClient httpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map<String, String> params = new HashMap<>();

        params.put("q", query);
        params.put("page", String.valueOf(PAGE_NUMBER));
        params.put("api_key", Constants.API_KEY);

        String sort = filterPreferences.getString("sort", "");
        String begin_date = filterPreferences.getString("begin_date", "");
        String end_date = filterPreferences.getString("end_date", "");
        String news_desk = filterPreferences.getString("news_desk", "");

        Log.d("NyTimes", "sort: " + sort + " begin_date:" + begin_date + " end_date:" + end_date + "news_desk:" + news_desk);

        if (!TextUtils.isEmpty(sort)) {
            params.put("sort", sort);
        }
        if (!TextUtils.isEmpty(begin_date)) {
            params.put("begin_date", begin_date);
        }
        if (!TextUtils.isEmpty(end_date)) {
            params.put("end_date", end_date);
        }
        if (!TextUtils.isEmpty(news_desk)) {
            params.put("fq", "news_desk:(" + news_desk + ")");
        }

        ArticleApiInterface apiService = retrofit.create(ArticleApiInterface.class);
        Call<Response> call = apiService.getArticles(params);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d(Constants.TAG, "onResponse: "+ response.code());
                if(response.code() == 200) {
                    Response r = response.body();
                    articleList.addAll(r.getDocument().getArticles());
                    articleAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } else {
                    Toast.makeText(ArticleListActivity.this, "status code"+ response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("DEBUG", "Network call failed ");
                Toast.makeText(ArticleListActivity.this, "Network call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year + " to " + dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
        etDateRange.setText(date);
    }
}
