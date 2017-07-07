package com.michaelfotiadis.mobiledota2.ui.image;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.steam.provider.image.ImageProvider;
import com.michaelfotiadis.steam.provider.image.Size;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import uk.co.alt236.resourcemirror.Mirror;

public class ImageLoader {

    private final Picasso mPicasso;
    private final ImageProvider mImageProvider;

    public ImageLoader(final Picasso picasso,
                       final ImageProvider imageProvider) {
        mPicasso = picasso;
        mImageProvider = imageProvider;
    }

    @DrawableRes
    private int getImageIdReflectively(final String drawableName, final ImageFamily family) {

        AppLog.d("Looking for drawable name: " + drawableName + " of family " + family);

        return Mirror.of("com.michaelfotiadis.dota2viewer").getDrawables().optListDrawableId(drawableName, getFamily(family), getFallback(family));

    }

    public void loadIntoImageView(@NonNull final ImageView view,
                                  @NonNull final String drawableName,
                                  @NonNull final ImageFamily type) {

        final int resId = getImageIdReflectively(drawableName.toLowerCase(), type);
        view.setImageResource(resId);

    }

    public void loadIntoImageView(final ImageView view, final String url) {

        if (TextUtils.isNotEmpty(url)) {
            if (TextUtils.isEmpty(view.getContentDescription())) {
                view.setContentDescription(url);
            }
            mPicasso.load(url).error(R.drawable.ic_default).into(view);
        } else {
            view.setImageDrawable(new IconicsDrawable(view.getContext(), FontAwesome.Icon.faw_steam).mutate());
        }
    }

    public void loadIntoImageView(final ImageView view, final Uri uri) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            view.setContentDescription(uri.toString());
        }
        mPicasso.load(uri).error(R.drawable.ic_default).into(view);
    }

    public void loadSteamGame(final ImageView view, final String appId, final String imageUrl) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            view.setContentDescription(imageUrl);
        }
        this.loadIntoImageView(view, mImageProvider.getGameImageEndpoint(appId, imageUrl));
    }

    public void loadHero(final ImageView view, final String name) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            view.setContentDescription(name);
        }
        this.loadIntoImageView(view, mImageProvider.getHeroImageEndpoint(name.replaceAll(" ", "_")));
    }

    public void loadHero(final ImageView view, final String name, final Size size) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            view.setContentDescription(name);
        }
        this.loadIntoImageView(view, mImageProvider.getHeroImageEndpoint(name.replaceAll(" ", "_"), size));
    }

    public void loadItem(final ImageView view, final String name) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            view.setContentDescription(name);
        }
        this.loadIntoImageView(view, mImageProvider.getItemImageEndpoint(name));
    }

    @DrawableRes
    private static int getFallback(final ImageFamily family) {

        switch (family) {

            case COUNTRY:
                return R.drawable.ic_list_country_unknown;
            case HERO:
                break;
            case ITEM:
                break;
            case MINI:
                break;
        }
        return R.drawable.ic_default;
    }

    private static String getFamily(final ImageFamily type) {

        switch (type) {

            case COUNTRY:
                return "country";
            case HERO:
                break;
            case ITEM:
                break;
            case MINI:
                break;
        }
        return "";

    }

    public enum ImageFamily {
        COUNTRY, HERO, ITEM, MINI
    }

}
