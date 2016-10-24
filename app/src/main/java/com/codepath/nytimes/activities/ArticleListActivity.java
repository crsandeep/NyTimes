package com.codepath.nytimes.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.codepath.nytimes.databinding.ActivityArticleListBinding;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.models.Response;
import com.codepath.nytimes.network.ArticleApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleListActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private ActivityArticleListBinding binding;

    private ArticleAdapter articleAdapter;
    private List<Article> articleList;
    private EditText etDateRange;

    private SharedPreferences filterPreferences;
    private SharedPreferences.Editor editor;

    private static Integer PAGE_NUMBER = 0;
    private static String QUERY = "";
    private static String TEMP_QUERY = "";
    private boolean isArtsChecked = false;
    private boolean isSportsChecked = false;
    private boolean isFashionChecked = false;
    private int sortOrder = 0;
    private String begin_date = "";
    private String end_date = "";

    Handler handler = new Handler();
    private boolean isFirst = true;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.nytimes);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articleList);
        binding.rvList.setAdapter(articleAdapter);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(mLayoutManager);

        ItemClickSupport.addTo(binding.rvList).setOnItemClickListener(
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

        QUERY = "";
        TEMP_QUERY = "";

        if (!isNetworkAvailable(this)) {
            Snackbar.make(binding.swipeContainer, R.string.not_connected, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                    v -> {
                        this.recreate();
                    }).show();
        }

        binding.rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener((StaggeredGridLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                PAGE_NUMBER++;
                fetchArticles(QUERY);
            }
        });

        binding.swipeContainer.setOnRefreshListener(() -> {
            PAGE_NUMBER = 0;
            fetchArticles(QUERY);
        });

        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            QUERY = query;
            TEMP_QUERY = query;
            isFirst = true;
            fetchArticles(query);
        } else {
            editor.clear().apply();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_article_list, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search articles..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PAGE_NUMBER = 0;
                QUERY = query;
                TEMP_QUERY = query;
                fetchArticles(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TEMP_QUERY = newText;
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

        CheckBox checkBoxArts = (CheckBox) dialogView.findViewById(R.id.checkBoxArts);
        CheckBox checkBoxFashion = (CheckBox) dialogView.findViewById(R.id.checkBoxFashion);
        CheckBox checkBoxSports = (CheckBox) dialogView.findViewById(R.id.checkBoxSports);
        Spinner oldNewSpinner = (Spinner) dialogView.findViewById(R.id.oldNewSpinner);
        etDateRange = (EditText) dialogView.findViewById(R.id.etDateRange);

        checkBoxArts.setChecked(filterPreferences.getBoolean("isArtsChecked", false));
        checkBoxFashion.setChecked(filterPreferences.getBoolean("isFashionChecked", false));
        checkBoxSports.setChecked(filterPreferences.getBoolean("isSportsChecked", false));
        oldNewSpinner.setSelection(filterPreferences.getInt("sortOrder", 0));

        if (!TextUtils.isEmpty(filterPreferences.getString("begin_date", ""))) {
            String tempDateRange = filterPreferences.getString("begin_date", "") + '-' + filterPreferences.getString("end_date", "");
            etDateRange.setText(tempDateRange);
        }

        dialogBuilder.setTitle("Filter");
        dialogBuilder.setPositiveButton("Apply", (DialogInterface dialog, int whichButton) -> {
            isArtsChecked = checkBoxArts.isChecked();
            editor.putBoolean("isArtsChecked", isArtsChecked);

            isFashionChecked = checkBoxFashion.isChecked();
            editor.putBoolean("isFashionChecked", isFashionChecked);

            isSportsChecked = checkBoxSports.isChecked();
            System.out.println("isSportsChecked " + isSportsChecked);
            editor.putBoolean("isSportsChecked", isSportsChecked);

            sortOrder = oldNewSpinner.getSelectedItemPosition();
            editor.putInt("sortOrder", sortOrder);

            if (!TextUtils.isEmpty(etDateRange.getText().toString())) {
                String temp_date[] = etDateRange.getText().toString().split("-");
                if (temp_date.length == 2) {
                    begin_date = temp_date[0];
                    end_date = temp_date[1];
                }
            } else {
                begin_date = "";
                end_date = "";
            }
            editor.putString("begin_date", begin_date);
            editor.putString("end_date", end_date);

            editor.apply();

            if (!TextUtils.isEmpty(TEMP_QUERY) || !TextUtils.isEmpty(QUERY)) {
                PAGE_NUMBER = 0;
                if (!TextUtils.isEmpty(TEMP_QUERY)) {
                    QUERY = TEMP_QUERY;
                }
                fetchArticles(QUERY);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", (DialogInterface dialog, int whichButton) -> {
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

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

        if (TextUtils.isEmpty(query)) {
            binding.swipeContainer.setRefreshing(false);
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

        int sort = filterPreferences.getInt("sortOrder", 0);
        String begin_date = filterPreferences.getString("begin_date", "");
        String end_date = filterPreferences.getString("end_date", "");
        String news_desk = filterPreferences.getString("news_desk", "");

        Log.d("NyTimes", "sort: " + sort + " begin_date:" + begin_date + " end_date:" + end_date + "news_desk:" + news_desk);

        if (sort != 0) {
            String sortString = "newest";
            if (sort == 2) {
                sortString = "oldest";
            }
            params.put("sort", sortString);
        }
        if (!TextUtils.isEmpty(begin_date)) {
            params.put("begin_date", begin_date);
        }
        if (!TextUtils.isEmpty(end_date)) {
            params.put("end_date", end_date);
        }
        if (isArtsChecked || isSportsChecked || isFashionChecked) {
            String temp = "news_desk:(";
            if (isArtsChecked) {
                temp += " Arts";
            }
            if (isSportsChecked) {
                temp += " Sports";
            }
            if (isFashionChecked) {
                temp += " Fashion%20%26%20Style";
            }
            temp += ")";
            Log.d(Constants.TAG, "fetchArticles: " + temp);
            params.put("fq", temp);
        }

        ArticleApiInterface apiService = retrofit.create(ArticleApiInterface.class);
        int delay;
        if(isFirst) {
            delay = 0;
        } else {
            delay = 1000;
        }
        handler.postDelayed(() -> {
            isFirst = false;
            Call<Response> call = apiService.getArticles(params);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Log.d(Constants.TAG, "onResponse: " + response.code());
                    if (response.code() == 200) {
                        Response r = response.body();
                        articleList.addAll(r.getDocument().getArticles());
                        articleAdapter.notifyDataSetChanged();
                        binding.swipeContainer.setRefreshing(false);
                    } else {
                        Toast.makeText(ArticleListActivity.this, "status code" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.e("DEBUG", "Network call failed ");
                    Toast.makeText(ArticleListActivity.this, "Network call failed", Toast.LENGTH_SHORT).show();
                }
            });
        }, delay);
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String day = Integer.toString(dayOfMonth);
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = Integer.toString(++monthOfYear);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String dayEnd = Integer.toString(dayOfMonthEnd);
        if (dayEnd.length() == 1) {
            dayEnd = "0" + dayEnd;
        }
        String monthEnd = Integer.toString(++monthOfYearEnd);
        if (monthEnd.length() == 1) {
            monthEnd = "0" + monthEnd;
        }
        String date = Integer.toString(year) + month + day + "-" + yearEnd + monthEnd + dayEnd;
        etDateRange.setText(date);
    }

    public void clearDate(View view) {
        etDateRange.setText("");
    }

    public void onDateSet(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ArticleListActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
}
