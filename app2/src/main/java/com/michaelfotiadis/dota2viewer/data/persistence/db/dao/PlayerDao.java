package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("select * from PlayerEntity")
    LiveData<List<PlayerEntity>> getAll();

    @Query("select * from PlayerEntity")
    List<PlayerEntity> getAllSync();

    @Query("select * from PlayerEntity where id = :id")
    LiveData<PlayerEntity> getById(String id);

    @Query("select * from PlayerEntity where id = :id")
    PlayerEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlayerEntity playerEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PlayerEntity> players);

    @Delete
    void delete(PlayerEntity playerModel);

    @Query("delete from PlayerEntity where id= :id")
    void delete(String id);

}