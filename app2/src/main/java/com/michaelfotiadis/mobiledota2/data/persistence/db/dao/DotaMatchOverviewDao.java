package com.michaelfotiadis.mobiledota2.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchOverviewEntity;

import java.util.List;

@Dao
public interface DotaMatchOverviewDao {

    @Query("select * from DotaMatchOverviewEntity")
    LiveData<List<DotaMatchOverviewEntity>> getAll();

    @Query("select * from DotaMatchOverviewEntity")
    List<DotaMatchOverviewEntity> getAllSync();

    @Query("select * from DotaMatchOverviewEntity where userId = :userId")
    List<DotaMatchOverviewEntity> getByUserIdSync(String userId);

    @Query("select * from DotaMatchOverviewEntity where id = :id")
    LiveData<DotaMatchOverviewEntity> getById(String id);

    @Query("select * from DotaMatchOverviewEntity where id = :id")
    DotaMatchOverviewEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaMatchOverviewEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaMatchOverviewEntity> matchOverviewEntities);

    @Delete
    void delete(DotaMatchOverviewEntity entity);

    @Query("delete from DotaMatchOverviewEntity where id= :id")
    void delete(String id);

}