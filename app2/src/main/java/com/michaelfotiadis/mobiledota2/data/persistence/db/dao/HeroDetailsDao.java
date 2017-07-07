package com.michaelfotiadis.mobiledota2.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.mobiledota2.data.persistence.db.model.HeroDetailsEntity;

import java.util.List;

@Dao
public interface HeroDetailsDao {

    @Query("select * from HeroDetailsEntity")
    LiveData<List<HeroDetailsEntity>> getAll();

    @Query("select * from HeroDetailsEntity")
    List<HeroDetailsEntity> getAllSync();

    @Query("select * from HeroDetailsEntity where id = :id")
    LiveData<HeroDetailsEntity> getById(String id);

    @Query("select * from HeroDetailsEntity where id = :id")
    HeroDetailsEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HeroDetailsEntity heroEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HeroDetailsEntity> heroes);

    @Delete
    void delete(HeroDetailsEntity heroEntity);

    @Query("delete from HeroDetailsEntity where id= :id")
    void delete(String id);

}