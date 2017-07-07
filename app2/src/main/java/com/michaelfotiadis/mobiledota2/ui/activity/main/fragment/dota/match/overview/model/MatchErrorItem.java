package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.overview.model;

import android.os.Parcel;

import com.michaelfotiadis.mobiledota2.data.persistence.error.UiDataLoadError;

public class MatchErrorItem implements MatchItem {

    private final Long mId;
    private final UiDataLoadError mError;

    public MatchErrorItem(final Long id, final UiDataLoadError error) {
        mId = id;
        mError = error;
    }

    @Override
    public long getItemId() {
        return mId;
    }

    public UiDataLoadError getError() {
        return mError;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeValue(this.mId);
        dest.writeSerializable(this.mError);
    }

    protected MatchErrorItem(final Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mError = (UiDataLoadError) in.readSerializable();
    }

    public static final Creator<MatchErrorItem> CREATOR = new Creator<MatchErrorItem>() {
        @Override
        public MatchErrorItem createFromParcel(final Parcel source) {
            return new MatchErrorItem(source);
        }

        @Override
        public MatchErrorItem[] newArray(final int size) {
            return new MatchErrorItem[size];
        }
    };
}
