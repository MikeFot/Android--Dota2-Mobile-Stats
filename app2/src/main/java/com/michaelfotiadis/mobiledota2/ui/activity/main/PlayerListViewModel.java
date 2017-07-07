package com.michaelfotiadis.mobiledota2.ui.activity.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.persistence.db.AppDatabase;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

import java.util.List;

import javax.inject.Inject;

public class PlayerListViewModel extends AndroidViewModel {

    @Inject
    AppDatabase mAppDatabase;
    @Inject
    JobScheduler mJobScheduler;

    private final LiveData<List<PlayerEntity>> mLiveData;

    public PlayerListViewModel(final Application application) {
        super(application);
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        mLiveData = mAppDatabase.getPlayerDao().getAll();
    }

    public LiveData<List<PlayerEntity>> getPlayers() {
        return mLiveData;
    }

    public void deleteProfile(final String userId) {
        AppLog.d("Deleting id " + userId);
        mJobScheduler.startDeleteProfileJob(userId);
    }

}