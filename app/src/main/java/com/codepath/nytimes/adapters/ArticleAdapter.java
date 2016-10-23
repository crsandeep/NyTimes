package com.codepath.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
            ah.tvTitle.setText(article.getHeadline().getMain());
            ah.imgArticle.setImageResource(0);
            Glide.with(mContext).load(article.getMultimedia().get(0).getThumbnailUrl()).placeholder(R.drawable.loading).into(((ArticleHolder) holder).imgArticle);
        } else {
            ArticleNoImageHolder ah = (ArticleNoImageHolder) holder;
            ah.tvTitle.setText(article.getHeadline().getMain());
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

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tvTitle) TextView tvTitle;
//        @BindView(R.id.imgArticle) ImageView imgArticle;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }

}
