package com.michaelfotiadis.mobiledota2.ui.activity.performance.fragment.calendar.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.adapter.BaseRecyclerViewAdapter;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewholder.BaseRecyclerViewHolder;
import com.michaelfotiadis.mobiledota2.ui.view.utils.ViewUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;

public class WinsRecyclerAdapter extends BaseRecyclerViewAdapter<Boolean, WinsRecyclerAdapter.WinsViewHolder> {

    private final WinsViewBinder mBinder;


    public WinsRecyclerAdapter(final Context context) {
        super(context);
        this.mBinder = new WinsViewBinder(context);
    }

    @Override
    protected boolean isItemValid(final Boolean item) {
        return item != null;
    }

    @Override
    public WinsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_win, parent, false);
        return new WinsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WinsViewHolder holder, final int position) {
        mBinder.bind(holder, getItem(position));
    }


    static class WinsViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.image)
        ImageView mImageView;

        WinsViewHolder(final View view) {
            super(view);
        }
    }

    private class WinsViewBinder extends BaseRecyclerViewBinder<WinsViewHolder, Boolean> {
        WinsViewBinder(final Context context) {
            super(context);
        }

        @Override
        protected void reset(final WinsViewHolder holder) {

        }

        @Override
        protected void setData(final WinsViewHolder holder, final Boolean value) {
            final Drawable drawable;
            final int tintColor;
            if (value) {
                drawable = new IconicsDrawable(getContext(), FontAwesome.Icon.faw_thumbs_up);
                tintColor = R.color.md_green_400;
            } else {
                drawable = new IconicsDrawable(getContext(), FontAwesome.Icon.faw_thumbs_down);
                tintColor = R.color.md_red_400;
            }
            ViewUtils.tintDrawable(drawable, ContextCompat.getColor(getContext(), tintColor));
            holder.mImageView.setImageDrawable(drawable);
        }
    }
}
