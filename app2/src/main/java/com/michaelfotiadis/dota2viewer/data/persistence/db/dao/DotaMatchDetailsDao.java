package com.michaelfotiadis.dota2viewer.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.dota2viewer.data.persistence.db.model.DotaMatchDetailsEntity;

import java.util.List;

@Dao
public interface DotaMatchDetailsDao {

    @Query("select * from DotaMatchDetailsEntity")
    LiveData<List<DotaMatchDetailsEntity>> getAll();

    @Query("select * from DotaMatchDetailsEntity")
    List<DotaMatchDetailsEntity> getAllSync();

    @Query("select * from DotaMatchDetailsEntity where id = :id")
    LiveData<DotaMatchDetailsEntity> getById(String id);

    @Query("select * from DotaMatchDetailsEntity where id = :id")
    DotaMatchDetailsEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DotaMatchDetailsEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DotaMatchDetailsEntity> entities);

    @Delete
    void delete(DotaMatchDetailsEntity entity);

    @Query("delete from DotaMatchDetailsEntity where id= :id")
    void delete(String id);

}