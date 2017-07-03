package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaLeagueEntity;

import java.util.List;

@Dao
public interface DotaLeagueDao {

    @Query("select * from DotaLeagueEntity")
    LiveData<List<DotaLeagueEntity>> getAll();

    @Query("select * from DotaLeagueEntity")
    List<DotaLeagueEntity> getAllSync();

    @Query("select * from DotaLeagueEntity where id = :id")
    LiveData<DotaLeagueEntity> getById(String id);

    @Query("select * from DotaLeagueEntity where id = :id")
    DotaLeagueEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaLeagueEntity leagueEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaLeagueEntity> leagues);

    @Delete
    void delete(DotaLeagueEntity leagueEntity);

    @Query("delete from DotaLeagueEntity where id= :id")
    void delete(String id);

}