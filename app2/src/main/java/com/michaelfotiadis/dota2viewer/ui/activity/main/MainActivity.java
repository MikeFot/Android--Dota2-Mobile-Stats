package com.michaelfotiadis.dota2viewer.ui.activity.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.loader.JobScheduler;
import com.michaelfotiadis.dota2viewer.data.persistence.db.accessor.DbAccessor;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.data.persistence.db.viewmodel.PlayerListViewModel;
import com.michaelfotiadis.dota2viewer.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.dota2viewer.event.steam.UserChangedEvent;
import com.michaelfotiadis.dota2viewer.event.steam.UserDeletedEvent;
import com.michaelfotiadis.dota2viewer.injection.Injector;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.DotaEconHeroesNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.DotaEconItemsNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.DotaMatchNavFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.SteamUserNavFragment;
import com.michaelfotiadis.dota2viewer.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.dota2viewer.ui.core.dialog.alert.AlertDialogFactory;
import com.michaelfotiadis.dota2viewer.ui.dialog.AboutDialog;
import com.michaelfotiadis.dota2viewer.ui.image.ImageLoader;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final int CONTENT_ID = R.id.content_frame;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    DbAccessor mDbAccessor;
    @Inject
    JobScheduler mJobScheduler;
    private Drawer mDrawer;
    private AccountHeader mAccountHeader;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);
        getEventLifecycleListener().enable();

        setTitle(R.string.app_name);

        init(savedInstanceState);

    }

    protected void init(final @Nullable Bundle savedInstanceState) {
        final PlayerListViewModel viewModel = ViewModelProviders.of(this).get(PlayerListViewModel.class);

        initDrawer(
                initDrawerFactory(savedInstanceState),
                getAccountHeader(Collections.<PlayerEntity>emptyList(), getCurrentUserId()));


        //Update the list when the data changes
        viewModel.getPlayers().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable final List<PlayerEntity> players) {
                if (players != null) {
                    AppLog.d("We have " + players.size() + " registered players");
                    AppLog.d("Current user id is: " + new UserPreferences(MainActivity.this).getCurrentUserId());
                    if (!players.isEmpty()) {
                        initDrawer(
                                initDrawerFactory(savedInstanceState),
                                getAccountHeader(players, getCurrentUserId()));
                    }
                } else {
                    AppLog.d("Cannot access players yet");
                }
            }
        });

        // cache the dota items and heroes
        if (TextUtils.isNotEmpty(getCurrentUserId())) {
            mJobScheduler.startFetchDotaHeroesJob();
            mJobScheduler.startFetchDotaItemsJob();
        }

        if (savedInstanceState == null) {
            mDrawer.setSelection(DrawerFactory.ID_STEAM, true);
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_container;
    }

    private DrawerFactory initDrawerFactory(final @Nullable Bundle savedInstanceState) {
        DrawerImageLoader.init(getDrawerImageLoader());
        return new DrawerFactory(this, savedInstanceState, getNavigationListener());
    }

    private AccountHeader getAccountHeader(final List<PlayerEntity> playerEntities,
                                           @Nullable final String currentUserId) {
        mAccountHeader = initDrawerFactory(null).getAccountHeader(playerEntities, currentUserId);
        return mAccountHeader;
    }

    private void initDrawer(final DrawerFactory drawerFactory,
                            final AccountHeader accountHeader) {
        if (!isFinishing()) {
            final DrawerBuilder drawerBuilder = drawerFactory.getDrawer((Toolbar) findViewById(R.id.toolbar));
            drawerBuilder.withAccountHeader(accountHeader);
            mDrawer = drawerBuilder.build();
        }
    }

    private DrawerImageLoader.IDrawerImageLoader getDrawerImageLoader() {
        return new DrawerImageLoader.IDrawerImageLoader() {

            @Override
            public void set(final ImageView imageView, final Uri uri, final Drawable placeholder) {
                if (!isFinishing()) {
                    mImageLoader.loadIntoImageView(imageView, uri);
                }

            }

            @Override
            public void set(final ImageView imageView, final Uri uri, final Drawable placeholder, final String tag) {
                if (!isFinishing()) {
                    mImageLoader.loadIntoImageView(imageView, uri);
                }
            }

            @Override
            public void cancel(final ImageView imageView) {

            }

            @Override
            public Drawable placeholder(final Context ctx) {
                return new IconicsDrawable(MainActivity.this, FontAwesome.Icon.faw_plus);
            }

            @Override
            public Drawable placeholder(final Context ctx, final String tag) {
                return new IconicsDrawable(MainActivity.this, FontAwesome.Icon.faw_plus);
            }

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DrawerImageLoader.getInstance().setImageLoader(null);
        mDrawer = null;
        mAccountHeader = null;
    }

    @NonNull
    private NavigationListener getNavigationListener() {
        return new NavigationListener() {

            @Override
            public void onSteamUserSelected() {
                AppLog.d("Showing Steam User Fragment");
                showSteamUserFragment();
                closeDrawer();
            }

            @Override
            public void onDotaMatchesSelected() {
                AppLog.d("Showing Dota Matches Fragment");
                showDotaMatchesFragment();
                closeDrawer();
            }

            @Override
            public void onDotaEconHeroesSelected() {
                AppLog.d("Showing Dota Econ Heroes Fragment");
                showDotaEconHeroesFragment();
                closeDrawer();
            }

            @Override
            public void onDotaEconItemsSelected() {
                AppLog.d("Showing Dota Econ Items Fragment");
                showDotaEconItemsFragment();
                closeDrawer();
            }

            @Override
            public void onRateSelected() {
                showRate();
            }

            @Override
            public void onAboutSelected() {
                showAbout();
            }

            @Override
            public void onAddProfile(final View view) {
                getIntentDispatcher().openLoginActivity(view);
            }

            @Override
            public void onProfileSelected(final View view, final long identifier) {

                final String textId = String.valueOf(identifier);

                if (!textId.equals(getCurrentUserId())) {
                    AppLog.d("User changed to id " + textId);
                    new UserPreferences(MainActivity.this).writeCurrentUserId(textId);
                    EventBus.getDefault().post(new UserChangedEvent(textId));
                    closeDrawer();
                }
            }

            @Override
            public void onDeleteProfile(final long identifier) {
                final DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        mJobScheduler.startDeleteProfileJob(String.valueOf(identifier));
                        dialog.dismiss();
                    }
                };
                final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialogFactory(MainActivity.this).show(
                        getString(R.string.dialog_delete_title),
                        getString(R.string.dialog_delete_user_body, String.valueOf(identifier)),
                        getString(R.string.message_ok), okListener,
                        getString(R.string.message_cancel), cancelListener);

            }
        };
    }

    private void showAbout() {
        new AboutDialog().show(getSupportFragmentManager(), AboutDialog.class.getSimpleName());
    }

    private void showRate() {
        //noinspection AnonymousInnerClassMayBeStatic
        new AlertDialog.Builder(this).setTitle(R.string.dialog_rate_title).setMessage(R.string.dialog_rate_message)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        getIntentDispatcher().openMarketRate(null);
                    }
                })
                .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void showDotaEconHeroesFragment() {

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            addContentFragmentIfMissing(DotaEconHeroesNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        } else if (!(fragment instanceof DotaEconHeroesNavFragment)) {
            replaceContentFragment(DotaEconHeroesNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        }

    }

    private void showDotaEconItemsFragment() {

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            addContentFragmentIfMissing(DotaEconItemsNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        } else if (!(fragment instanceof DotaEconItemsNavFragment)) {
            replaceContentFragment(DotaEconItemsNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        }

    }

    private void showDotaMatchesFragment() {

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            addContentFragmentIfMissing(DotaMatchNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        } else if (!(fragment instanceof DotaMatchNavFragment)) {
            replaceContentFragment(DotaMatchNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        }
    }

    private void showSteamUserFragment() {

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            addContentFragmentIfMissing(SteamUserNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        } else if (!(fragment instanceof SteamUserNavFragment)) {
            replaceContentFragment(SteamUserNavFragment.newInstance(), CONTENT_ID, FRAGMENT_TAG);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mDrawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = mAccountHeader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserDeletedEvent(final UserDeletedEvent event) {

        initDrawer(
                initDrawerFactory(null),
                getAccountHeader(event.getPlayerEntities(), event.getNextId()));
        EventBus.getDefault().post(new UserChangedEvent(getCurrentUserId()));
    }

    private void closeDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer();
        }
    }

    public static Intent newInstance(final Context context) {
        return new Intent(context, MainActivity.class);
    }

}
