package com.michaelfotiadis.dota2viewer.event.steam;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.event.Event;

import java.util.List;

public class UserDeletedEvent implements Event {

    private final List<PlayerEntity> playerEntities;
    private final String mNextId;


    public UserDeletedEvent(final List<PlayerEntity> playerEntities, final String nextId) {
        this.playerEntities = playerEntities;
        mNextId = nextId;
    }

    public List<PlayerEntity> getPlayerEntities() {
        return playerEntities;
    }

    public String getNextId() {
        return mNextId;
    }
}
