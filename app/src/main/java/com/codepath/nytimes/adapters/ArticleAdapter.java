package com.codepath.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;

    private int ARTICLE = 1;

    public ArticleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mArticles.get(position).hasThumbnail()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);

        if(holder.getItemViewType() == ARTICLE) {
            ArticleHolder ah = (ArticleHolder) holder;
            article.thumbnailUrl = article.getMultimedia().get(0).getThumbnailUrl();
            ah.binding.setArticle(article);
        } else {
            ArticleNoImageHolder ah = (ArticleNoImageHolder) holder;
            ah.binding.setArticle(article);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == ARTICLE) {
            view = inflater.inflate(R.layout.item_article, parent, false);
            viewHolder = new ArticleHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_article_no_image, parent, false);
            viewHolder = new ArticleNoImageHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

//    @BindingAdapterUtils({"bind:imageUrl"})
//    public static void loadImage(ImageView view, String url) {
//        view.setImageResource(0);
//        Glide.with(view.getContext()).load("http://www.nytimes.com/" + url).placeholder(R.drawable.loading).into(view);
//    }

}
