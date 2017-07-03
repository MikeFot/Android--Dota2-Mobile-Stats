package com.michaelfotiadis.dota2viewer.data.persistence.db.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.michaelfotiadis.dota2viewer.data.persistence.db.DatabaseCreator;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.utils.AppLog;

import java.util.List;

public class PlayerListViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    static {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<List<PlayerEntity>> mObservablePlayers;

    public PlayerListViewModel(final Application application) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance();

        final LiveData<Boolean> databaseCreated = databaseCreator.isDatabaseCreated();
        mObservablePlayers = Transformations.switchMap(databaseCreated,
                new Function<Boolean, LiveData<List<PlayerEntity>>>() {
                    @Override
                    public LiveData<List<PlayerEntity>> apply(final Boolean isDbCreated) {
                        //noinspection IfStatementWithNegatedCondition
                        if (!Boolean.TRUE.equals(isDbCreated)) { // Not needed here, but watch out for null
                            AppLog.d("ViewModel: DB Absent");
                            //noinspection unchecked
                            return ABSENT;
                        } else {
                            AppLog.d("ViewModel: DB Created");
                            //noinspection ConstantConditions
                            return databaseCreator.getDatabase().getPlayerDao().getAll();
                        }
                    }
                });

        databaseCreator.createDb(this.getApplication());
    }

    /**
     * Expose the LiveData Players query so the UI can observe it.
     */
    public LiveData<List<PlayerEntity>> getPlayers() {
        return mObservablePlayers;
    }
}