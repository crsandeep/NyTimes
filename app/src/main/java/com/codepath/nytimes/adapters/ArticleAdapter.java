package com.codepath.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;

    public ArticleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        TextView tvWebLink = holder.tvWebLink;
        tvWebLink.setText(article.getHeadline().getMain());
        holder.imgArticle.setImageResource(0);
        if(article.getMultimedia() != null && article.getMultimedia().size()>0) {
            holder.imgArticle.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(article.getMultimedia().get(0).getThumbnailUrl()).into(holder.imgArticle);
        } else {
            holder.imgArticle.setVisibility(View.GONE);
        }
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvWebLink) TextView tvWebLink;
        @BindView(R.id.imgArticle) ImageView imgArticle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
