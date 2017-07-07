package com.michaelfotiadis.mobiledota2.data.persistence.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaHeroDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaItemDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaLeagueDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaMatchDetailsDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaMatchOverviewDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.DotaRarityDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.HeroDetailsDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.HeroPatchAttributesDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.LibraryDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.dao.PlayerDao;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaHeroEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaItemEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaLeagueEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchDetailsEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaMatchOverviewEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.DotaRarityEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.HeroDetailsEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.HeroPatchAttributesEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.LibraryEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.PlayerEntity;

@Database(
        entities = {
                PlayerEntity.class,
                DotaHeroEntity.class,
                DotaItemEntity.class,
                DotaRarityEntity.class,
                HeroDetailsEntity.class,
                LibraryEntity.class,
                DotaMatchOverviewEntity.class,
                DotaMatchDetailsEntity.class,
                HeroPatchAttributesEntity.class,
                DotaLeagueEntity.class
        },
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "dota_app_db";

    public abstract PlayerDao getPlayerDao();

    public abstract LibraryDao getLibraryDao();

    public abstract DotaHeroDao getDotaHeroDao();

    public abstract DotaItemDao getDotaItemDao();

    public abstract DotaRarityDao getDotaRarityDao();

    public abstract HeroDetailsDao getHeroDetailsDao();

    public abstract DotaMatchOverviewDao getDotaMatchOverviewDao();

    public abstract DotaMatchDetailsDao getDotaMatchDetailsDao();

    public abstract HeroPatchAttributesDao getHeroPatchAttributesDao();

    public abstract DotaLeagueDao getDotaLeagueDao();

}