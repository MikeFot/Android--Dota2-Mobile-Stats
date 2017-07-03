package com.michaelfotiadis.dota2viewer.data.loader.jobs.dota.stats;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.error.Error;
import com.michaelfotiadis.dota2viewer.data.loader.error.ErrorKind;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.BaseJob;
import com.michaelfotiadis.dota2viewer.data.persistence.db.dao.HeroPatchAttributesDao;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.HeroPatchAttributesEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.model.HeroPatchAttributes;
import com.michaelfotiadis.dota2viewer.event.dota.stats.FetchedDotaHeroPatchAttributesEvent;
import com.michaelfotiadis.dota2viewer.utils.AppLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchDotaHeroPatchAttrsJob extends BaseJob {


    private final Resources mResources;
    private final HeroPatchAttributesDao mDao;

    public FetchDotaHeroPatchAttrsJob(final Resources resources,
                                      final HeroPatchAttributesDao dao) {
        mResources = resources;
        mDao = dao;
    }

    @Override
    public void onAdded() {
        AppLog.d(FetchDotaHeroPatchAttrsJob.class.getSimpleName() + " job added on " + System.currentTimeMillis());
    }

    @Override
    public void onRun() throws Throwable {

        final List<HeroPatchAttributesEntity> entities = mDao.getAllSync();

        // TODO add caching as well
        if (entities == null || entities.isEmpty()) {

            final InputStream in = mResources.openRawResource(R.raw.herostats_705);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            final StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append('\n');
                }

                final String content = sb.toString();
                final Type listType = new ArrayListTypeToken().getType();

                final List<HeroPatchAttributes> result = new Gson().fromJson(content, listType);

                mDao.insert(HeroPatchAttributesEntity.fromHeroes(result));

                postEvent(new FetchedDotaHeroPatchAttributesEvent(result));
                postJobFinished();

            } catch (final IOException e) {
                postError(new Error(ErrorKind.INVALID_CONTENT));
            }
        } else {
            final List<HeroPatchAttributes> attributes = HeroPatchAttributesEntity.fromEntities(entities);
            postEvent(new FetchedDotaHeroPatchAttributesEvent(attributes));
            postJobFinished();
        }


    }

    @Override
    protected void onCancel(final int cancelReason, @Nullable final Throwable throwable) {
        AppLog.d("Job cancelled for reason: " + cancelReason);
        postError(new Error(ErrorKind.NO_CONTENT_RETURNED));
    }

    private void postError(final Error error) {
        postEvent(new FetchedDotaHeroPatchAttributesEvent(Collections.<HeroPatchAttributes>emptyList(), error));
        postJobFinished();
    }

    private static class ArrayListTypeToken extends TypeToken<ArrayList<HeroPatchAttributes>> {
    }
}
