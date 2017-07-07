package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.profile.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.event.steam.FetchedPlayersEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.utils.AppLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class SteamProfileViewModel extends AndroidViewModel {

    @Inject
    JobScheduler mJobScheduler;

    private MutableLiveData<SteamProfilePayload> liveData;

    public SteamProfileViewModel(final Application application) {
        super(application);
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
    }

    public LiveData<SteamProfilePayload> getProfile(final String userId) {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            loadProfile(userId);
        }
        return liveData;
    }

    public void loadProfile(final String userId) {
        AppLog.d("ViewModel Loading data for id " + userId);
        mJobScheduler.startFetchPlayersJob(userId, true);
    }

    public void deleteProfile(final String userId) {
        AppLog.d("Deleting id " + userId);
        mJobScheduler.startDeleteProfileJob(userId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedPlayersEvent event) {
        AppLog.d("ViewModel new live data received with hashcode " + event.hashCode());
        liveData.setValue(new SteamProfilePayload(event.getPlayers(), event.getError()));
    }

}