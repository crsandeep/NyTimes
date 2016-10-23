package com.codepath.nytimes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimes.databinding.ItemArticleBinding;

public class ArticleHolder extends RecyclerView.ViewHolder {

    public ItemArticleBinding binding;

    public TextView tvTitle;
    public ImageView imgArticle;

    public ArticleHolder(View itemView) {
        super(itemView);
        binding = ItemArticleBinding.bind(itemView);
        tvTitle = binding.tvTitle;
        imgArticle = binding.imgArticle;
    }
}
