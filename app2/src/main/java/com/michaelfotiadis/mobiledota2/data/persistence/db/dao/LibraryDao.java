package com.michaelfotiadis.mobiledota2.data.persistence.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaelfotiadis.mobiledota2.data.persistence.db.model.LibraryEntity;

import java.util.List;

@Dao
public interface LibraryDao {

    @Query("select * from LibraryEntity")
    LiveData<List<LibraryEntity>> getAll();

    @Query("select * from LibraryEntity")
    List<LibraryEntity> getAllSync();

    @Query("select * from LibraryEntity where id = :id")
    LiveData<LibraryEntity> getById(String id);

    @Query("select * from LibraryEntity where id = :id")
    LibraryEntity getByIdSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LibraryEntity playerEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LibraryEntity> players);

    @Delete
    void delete(LibraryEntity entity);

    @Query("delete from LibraryEntity where id= :id")
    void delete(String id);

}