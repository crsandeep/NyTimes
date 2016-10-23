package com.codepath.nytimes.adapters;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        view.setImageResource(0);
        Glide.with(view.getContext()).load(url).placeholder(R.drawable.loading).into(view);
    }
}