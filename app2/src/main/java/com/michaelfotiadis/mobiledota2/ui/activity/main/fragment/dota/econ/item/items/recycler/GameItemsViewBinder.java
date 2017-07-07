package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.items.recycler;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.listener.OnItemSelectedListener;
import com.michaelfotiadis.mobiledota2.ui.core.base.recyclerview.viewbinder.BaseRecyclerViewBinder;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.ui.view.utils.ViewUtils;
import com.michaelfotiadis.steam.data.dota2.model.item.GameItem;

class GameItemsViewBinder extends BaseRecyclerViewBinder<GameItemsViewHolder, GameItem> {

    private final ImageLoader mImageLoader;
    private final OnItemSelectedListener<GameItem> mListener;

    GameItemsViewBinder(final Context context,
                        final ImageLoader imageLoader,
                        final OnItemSelectedListener<GameItem> listener) {
        super(context);
        mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    protected void reset(final GameItemsViewHolder holder) {
        holder.mTextView.setText("");

        holder.getRoot().setOnClickListener(null);
    }

    @Override
    protected void setData(final GameItemsViewHolder holder, final GameItem item) {

        holder.mTextView.setText(item.getLocalizedName());

        mImageLoader.loadItem(holder.mImageView, item.getName());


        if (item.getCost() != null) {
            holder.mTextCost.setText(getContext().getString(R.string.item_cost, item.getCost().toString()));
        }


        setCheck(holder.mTextSideShop, item.getSideShop());
        setCheck(holder.mTextSecretShop, item.getSecretShop());

        holder.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onListItemSelected(v, item);
            }
        });
    }

    private void setCheck(final TextView textView, final Integer value) {

        final Drawable drawable;
        if (value != null && value.equals(1)) {
            drawable = getDrawable(R.drawable.ic_check_circle_white_24dp);
            drawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.md_green_500), PorterDuff.Mode.MULTIPLY));
        } else {
            drawable = getDrawable(R.drawable.ic_cancel_white_24dp);
            drawable.setColorFilter(new PorterDuffColorFilter(getColor(R.color.md_red_500), PorterDuff.Mode.MULTIPLY));


        }
        ViewUtils.setDrawableLeft(textView, drawable);
    }
}
