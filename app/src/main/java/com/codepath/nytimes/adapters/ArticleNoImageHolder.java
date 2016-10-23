package com.codepath.nytimes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.nytimes.R;

public class ArticleNoImageHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;

    public ArticleNoImageHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
    }
}
