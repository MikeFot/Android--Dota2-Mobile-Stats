package com.michaelfotiadis.dota2viewer.ui.view.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapContainer implements Parcelable {
    private final float mX;
    private final float mY;
    private final boolean mIsDestroyed;
    private final boolean mIsTower;
    private final boolean mIsRadiant;

    public MapContainer(final float x, final float y, final boolean isDestroyed, final boolean isTower, final boolean mIsRadiant) {
        this.mX = x;
        this.mY = y;
        this.mIsDestroyed = isDestroyed;
        this.mIsTower = isTower;
        this.mIsRadiant = mIsRadiant;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public boolean isRadiant() {
        return mIsRadiant;
    }

    public boolean isTower() {
        return mIsTower;
    }

    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mX);
        dest.writeFloat(this.mY);
        dest.writeByte(mIsDestroyed ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsTower ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsRadiant ? (byte) 1 : (byte) 0);
    }

    protected MapContainer(Parcel in) {
        this.mX = in.readFloat();
        this.mY = in.readFloat();
        this.mIsDestroyed = in.readByte() != 0;
        this.mIsTower = in.readByte() != 0;
        this.mIsRadiant = in.readByte() != 0;
    }

    public static final Creator<MapContainer> CREATOR = new Creator<MapContainer>() {
        public MapContainer createFromParcel(Parcel source) {
            return new MapContainer(source);
        }

        public MapContainer[] newArray(int size) {
            return new MapContainer[size];
        }
    };
}