package com.codepath.nytimes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimes.R;

public class ArticleHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public ImageView imgArticle;

    public ArticleHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        imgArticle = (ImageView) itemView.findViewById(R.id.imgArticle);
    }
}
