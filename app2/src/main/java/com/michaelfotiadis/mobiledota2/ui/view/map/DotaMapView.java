package com.michaelfotiadis.mobiledota2.ui.view.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.michaelfotiadis.mobiledota2.R;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Michael on 25/05/2015
 * Modified 06/2017
 */
public class DotaMapView extends AppCompatImageView {

    private Collection<MapContainer> mMapCollection;
    private Drawable mTowerDrawable;
    private Drawable mBarracksDrawable;

    public DotaMapView(final Context context) {
        this(context, null);
    }

    public DotaMapView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotaMapView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMapList(final Collection<MapContainer> mapList) {
        this.mMapCollection = mapList;
        this.invalidate();
    }

    private void init() {
        mTowerDrawable = getDrawable(R.drawable.icon_tower_chess);
        mBarracksDrawable = getDrawable(R.drawable.icon_barracks);
        setAdjustViewBounds(true);

        mMapCollection = isInEditMode() ? new MapManager().getAsList() : Collections.<MapContainer>emptyList();
    }

    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        super.onDraw(canvas);

        final int dWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        final int dHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        final int radius = dHeight / 50;

        for (final MapContainer mapObject : mMapCollection) {
            if (mapObject != null) {

                if (mapObject.isTower()) {
                    addDrawableBounds(mTowerDrawable, radius, dWidth, dHeight, mapObject);
                    addDrawableColourFilter(mTowerDrawable, mapObject);
                    mTowerDrawable.draw(canvas);
                } else {
                    addDrawableBounds(mBarracksDrawable, radius, dWidth, dHeight, mapObject);
                    addDrawableColourFilter(mBarracksDrawable, mapObject);
                    mBarracksDrawable.draw(canvas);
                }
            }
        }

        this.invalidate();
    }

    private void addDrawableBounds(final Drawable drawable, final int radius, final int width, final int height, final MapContainer mapObject) {
        // handle the initial drawing
        final float mapObjX = mapObject.getX();
        final float mapObjY = mapObject.getY();
        drawable.setBounds(
                (int) (width * mapObjX - radius + getPaddingLeft()),
                (int) (height * mapObjY - radius + getPaddingTop()),
                (int) (width * mapObjX + radius + getPaddingLeft()),
                (int) (height * mapObjY + radius + getPaddingTop()));
    }

    private void addDrawableColourFilter(final Drawable drawable, final MapContainer mapObject) {
        //TODO
        if (mapObject.isRadiant() && !mapObject.isDestroyed()) {
            drawable.setColorFilter(getColor(R.color.md_green_700), PorterDuff.Mode.SRC_ATOP);
        } else if (mapObject.isRadiant()) {
            drawable.setColorFilter(getColor(R.color.md_grey_300), PorterDuff.Mode.SRC_ATOP);
        } else if (!mapObject.isDestroyed()) {
            drawable.setColorFilter(getColor(R.color.md_red_700), PorterDuff.Mode.SRC_ATOP);
        } else {
            drawable.setColorFilter(getColor(R.color.md_grey_400), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private Drawable getDrawable(@DrawableRes final int drawableRes) {
        return ContextCompat.getDrawable(getContext(), drawableRes);
    }

    @ColorInt
    private int getColor(@ColorRes final int color) {
        return ContextCompat.getColor(getContext(), color);
    }

}
