package com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.library.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.event.steam.FetchedLibraryEvent;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.michaelfotiadis.mobiledota2.utils.TextUtils;
import com.michaelfotiadis.mobiledota2.utils.dota.SearchFilterUtils;
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