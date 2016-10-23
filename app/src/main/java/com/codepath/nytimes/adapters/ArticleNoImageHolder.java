package com.codepath.nytimes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.nytimes.databinding.ItemArticleNoImageBinding;

public class ArticleNoImageHolder extends RecyclerView.ViewHolder {

    public ItemArticleNoImageBinding binding;
    public TextView tvTitle;

    public ArticleNoImageHolder(View itemView) {
        super(itemView);
        binding = ItemArticleNoImageBinding.bind(itemView);
        tvTitle = binding.tvTitle;
    }
}
