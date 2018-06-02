package com.lbrand.githubclient.common;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lbrand.githubclient.R;


public class BindingUtils {

    private static final String TAG = BindingUtils.class.getSimpleName();

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url)
                .placeholder(R.drawable.image_paceholder)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .into(view);
    }
    @BindingAdapter({"app:icon"})
    public static void loadIcon(ImageView view, String url) {
        Glide.with(view.getContext()).load(url)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .into(view);
    }
    @BindingAdapter({"refreshing"})
    public static void setRefreshing(SwipeRefreshLayout view, boolean isRefreshing) {
        view.setRefreshing(isRefreshing);
    }
}
