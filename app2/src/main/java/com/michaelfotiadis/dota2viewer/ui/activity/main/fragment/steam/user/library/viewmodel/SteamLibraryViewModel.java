package com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.event.steam.FetchedLibraryEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.dota2viewer.utils.dota.SearchFilterUtils;
import com.michaelfotiadis.steam.data.steam.player.library.Game;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SteamLibraryViewModel extends AndroidViewModel {

    private final List<Game> mData = new ArrayList<>();
    @Inject
    JobScheduler mJobScheduler;

    private MutableLiveData<SteamLibraryPayload> liveData;

    public SteamLibraryViewModel(final Application application) {
        super(application);
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
    }

    public LiveData<SteamLibraryPayload> getGames(final String userId) {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            AppLog.d("Initial loading");
            loadGames(userId);
        }
        return liveData;
    }

    public void loadGames(final String userId) {
        AppLog.d("ViewModel Loading data for id " + userId);
        mData.clear();
        if (TextUtils.isEmpty(userId)) {
            liveData.setValue(new SteamLibraryPayload(mData, null));
        } else {
            mJobScheduler.startFetchLibraryJob(userId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataLoadedEvent(final FetchedLibraryEvent event) {
        AppLog.d("ViewModel new live data received with hashcode " + event.hashCode());
        mData.clear();
        mData.addAll(event.getGames());
        AppLog.d("Total of " + event.getGames().size() + " games");
        liveData.setValue(new SteamLibraryPayload(event.getGames(), event.getError()));
    }

    public void setQuery(final String query) {

        if (TextUtils.isEmpty(query)) {
            liveData.setValue(new SteamLibraryPayload(mData, null));
        } else {
            liveData.setValue(new SteamLibraryPayload(new ArrayList<>(SearchFilterUtils.getFilteredLibrary(mData, query)), null));
        }

    }

}