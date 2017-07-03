package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaRarityEntity;

import java.util.List;

@Dao
public interface DotaRarityDao {

    @Query("select * from DotaRarityEntity")
    LiveData<List<DotaRarityEntity>> getAll();

    @Query("select * from DotaRarityEntity")
    List<DotaRarityEntity> getAllSync();

    @Query("select * from DotaRarityEntity where id = :id")
    LiveData<DotaRarityEntity> getById(String id);

    @Query("select * from DotaRarityEntity where id = :id")
    DotaRarityEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaRarityEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaRarityEntity> entities);

    @Delete
    void delete(DotaRarityEntity entity);

    @Query("delete from DotaRarityEntity where id= :id")
    void delete(String id);

}