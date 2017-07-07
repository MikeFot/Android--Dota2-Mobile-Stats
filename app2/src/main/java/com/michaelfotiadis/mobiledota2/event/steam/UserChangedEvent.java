package com.michaelfotiadis.mobiledota2.event.steam;

import com.michaelfotiadis.mobiledota2.event.Event;

public class UserChangedEvent implements Event {

    private final String mId;


    public UserChangedEvent(final String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

}
