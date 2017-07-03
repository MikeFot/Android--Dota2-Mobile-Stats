package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaItemEntity;

import java.util.List;

@Dao
public interface DotaItemDao {

    @Query("select * from DotaItemEntity")
    LiveData<List<DotaItemEntity>> getAll();

    @Query("select * from DotaItemEntity")
    List<DotaItemEntity> getAllSync();

    @Query("select * from DotaItemEntity where id = :id")
    LiveData<DotaItemEntity> getById(String id);

    @Query("select * from DotaItemEntity where id = :id")
    DotaItemEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaItemEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaItemEntity> entities);

    @Delete
    void delete(DotaItemEntity entity);

    @Query("delete from DotaItemEntity where id= :id")
    void delete(String id);

}