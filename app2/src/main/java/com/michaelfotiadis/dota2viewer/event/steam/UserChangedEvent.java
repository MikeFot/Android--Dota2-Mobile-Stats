package com.michaelfotiadis.dota2viewer.event.steam;

import com.michaelfotiadis.dota2viewer.event.Event;

public class UserChangedEvent implements Event {

    private final String mId;


    public UserChangedEvent(final String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

}
