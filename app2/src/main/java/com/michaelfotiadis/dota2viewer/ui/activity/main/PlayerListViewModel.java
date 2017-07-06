package com.michaelfotiadis.dota2viewer.ui.activity.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.michaelfotiadis.dota2viewer.data.persistence.db.AppDatabase;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.injection.Injector;

import java.util.List;

import javax.inject.Inject;

public class PlayerListViewModel extends AndroidViewModel {

    @Inject
    AppDatabase mAppDatabase;

    private final LiveData<List<PlayerEntity>> mLiveData;

    public PlayerListViewModel(final Application application) {
        super(application);
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        mLiveData = mAppDatabase.getPlayerDao().getAll();
    }

    public LiveData<List<PlayerEntity>> getPlayers() {
        return mLiveData;
    }
}