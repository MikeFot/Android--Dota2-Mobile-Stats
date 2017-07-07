package com.michaelfotiadis.mobiledota2.ui.activity.login.fragment.popular;

public class PopularPlayer {

    private final String mName;
    private final String mId;

    public PopularPlayer(final String name, final String id) {
        mName = name;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }
}
