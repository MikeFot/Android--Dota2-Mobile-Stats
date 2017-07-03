package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.HeroPatchAttributesEntity;

import java.util.List;

@Dao
public interface HeroPatchAttributesDao {

    @Query("select * from HeroPatchAttributesEntity")
    LiveData<List<HeroPatchAttributesEntity>> getAll();

    @Query("select * from HeroPatchAttributesEntity")
    List<HeroPatchAttributesEntity> getAllSync();

    @Query("select * from HeroPatchAttributesEntity where id = :id")
    LiveData<HeroPatchAttributesEntity> getById(String id);

    @Query("select * from HeroPatchAttributesEntity where id = :id")
    HeroPatchAttributesEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HeroPatchAttributesEntity heroEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HeroPatchAttributesEntity> heroes);

    @Delete
    void delete(HeroPatchAttributesEntity heroEntity);

    @Query("delete from HeroPatchAttributesEntity where id= :id")
    void delete(String id);

}