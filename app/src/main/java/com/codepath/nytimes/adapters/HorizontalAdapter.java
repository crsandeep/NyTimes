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
import com.codepath.nytimes.models.PopularArticle;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private List<PopularArticle> horizontalList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView txtView;
        public TextView tvPopTitle;

        public MyViewHolder(View view) {
            super(view);
            txtView = (ImageView) view.findViewById(R.id.txtView);
            tvPopTitle = (TextView) view.findViewById(R.id.tvPopTitle);
        }
    }

    public HorizontalAdapter(Context context, List<PopularArticle> horizontalList) {
        this.horizontalList = horizontalList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(mContext).load(horizontalList.get(position).imageUrl).into(holder.txtView);
        holder.tvPopTitle.setText(horizontalList.get(position).title);
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}