package com.sample;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by cisner-1 on 9/3/18.
 */

public class BaseBinder {
    @BindingAdapter({"bind:imgUrl"})
    public static void setImageUrl(ImageView view, String url) {
        GlideApp.with(view.getContext())
                .load(url == null ? "" : url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .timeout(60000)
                .into(view);
    }
}