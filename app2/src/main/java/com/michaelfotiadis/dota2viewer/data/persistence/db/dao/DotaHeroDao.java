package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaHeroEntity;

import java.util.List;

@Dao
public interface DotaHeroDao {

    @Query("select * from DotaHeroEntity")
    LiveData<List<DotaHeroEntity>> getAll();

    @Query("select * from DotaHeroEntity")
    List<DotaHeroEntity> getAllSync();

    @Query("select * from DotaHeroEntity where name = :name")
    LiveData<DotaHeroEntity> getById(String name);

    @Query("select * from DotaHeroEntity where name = :name")
    DotaHeroEntity getByIdSync(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaHeroEntity heroEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaHeroEntity> heroes);

    @Delete
    void delete(DotaHeroEntity heroEntity);

    @Query("delete from DotaHeroEntity where name= :name")
    void delete(String name);

}