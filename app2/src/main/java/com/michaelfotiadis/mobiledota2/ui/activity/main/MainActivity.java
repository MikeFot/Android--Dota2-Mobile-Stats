package com.michaelfotiadis.mobiledota2.ui.activity.main;

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

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.michaelfotiadis.mobiledota2.R;
import com.michaelfotiadis.mobiledota2.data.loader.JobScheduler;
import com.michaelfotiadis.mobiledota2.data.persistence.db.DatabaseCreator;
import com.michaelfotiadis.mobiledota2.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.mobiledota2.data.persistence.preference.UserPreferences;
import com.michaelfotiadis.mobiledota2.injection.Injector;
import com.michaelfotiadis.mobiledota2.network.ConnectivityUtils;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.hero.DotaEconHeroesNavFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.econ.item.DotaEconItemsNavFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.dota.match.DotaMatchNavFragment;
import com.michaelfotiadis.mobiledota2.ui.activity.main.fragment.steam.user.SteamUserNavFragment;
import com.michaelfotiadis.mobiledota2.ui.core.base.activity.BaseActivity;
import com.michaelfotiadis.mobiledota2.ui.core.dialog.alert.AlertDialogFactory;
import com.michaelfotiadis.mobiledota2.ui.dialog.AboutDialog;
import com.michaelfotiadis.mobiledota2.ui.image.ImageLoader;
import com.michaelfotiadis.mobiledota2.utils.AppLog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final int CONTENT_ID = R.id.content_frame;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    JobScheduler mJobScheduler;
    @Inject
    UserPreferences mUserPreferences;

    private PlayerListViewModel mViewModel;
    private Drawer mDrawer;
    private AccountHeader mAccountHeader;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        Injector.getComponentStore().getAndroidAwareComponent().inject(this);
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        setTitle(R.string.app_name);

        DatabaseCreator.getInstance().isDatabaseCreated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isCreated) {
                if (isCreated != null && isCreated) {
                    AppLog.d("DB is ready - initialising MainActivity");
                    init(savedInstanceState);
                }
            }
        });
        DatabaseCreator.getInstance().createDb(getApplicationContext());


    }

    protected void init(final @Nullable Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(this).get(PlayerListViewModel.class);

        //Update the list when the data changes
        mViewModel.getPlayers().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable final List<PlayerEntity> players) {

                if (players != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppLog.d("We have " + players.size() + " registered players");
                            AppLog.d("Current user id is: " + getCurrentUserId());
                            initDrawer(
                                    initDrawerFactory(savedInstanceState),
                                    getAccountHeader(players, getCurrentUserId()));
                            if (savedInstanceState == null) {
                                mDrawer.setSelection(DrawerFactory.ID_STEAM, true);
                            }
                        }
                    });
                } else {
                    AppLog.d("Cannot access players yet");
                }
            }
        });

        if (mUserPreferences.getIsFirstRun() && ConnectivityUtils.isConnected(this)) {
            mJobScheduler.startFetchDotaHeroesJob();
            mJobScheduler.startFetchDotaItemsJob();
            mUserPreferences.writeIsFirstRun(false);
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
            closeDrawer();
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
                showFragment(SteamUserNavFragment.newInstance());
            }

            @Override
            public void onDotaMatchesSelected() {
                AppLog.d("Showing Dota Matches Fragment");
                showFragment(DotaMatchNavFragment.newInstance());
            }

            @Override
            public void onDotaEconHeroesSelected() {
                AppLog.d("Showing Dota Econ Heroes Fragment");
                showFragment(DotaEconHeroesNavFragment.newInstance());
            }

            @Override
            public void onDotaEconItemsSelected() {
                AppLog.d("Showing Dota Econ Items Fragment");
                showFragment(DotaEconItemsNavFragment.newInstance());
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
                    mUserPreferences.writeCurrentUserId(textId);
                    closeDrawer();
                }
            }

            @Override
            public void onDeleteProfile(final long identifier) {
                final DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        mViewModel.deleteProfile(String.valueOf(identifier));
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
                        Answers.getInstance().logCustom(new CustomEvent("Rate - OK"));
                        getIntentDispatcher().openMarketRate(null);
                    }
                })
                .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Answers.getInstance().logCustom(new CustomEvent("Rate - Cancelled"));
                        dialog.dismiss();
                    }
                }).show();

    }

    private void showFragment(final Fragment fragment) {
        replaceContentFragment(fragment, CONTENT_ID, FRAGMENT_TAG);
        closeDrawer();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mDrawer != null && mAccountHeader != null) {
            //add the values which need to be saved from the drawer to the bundle
            outState = mDrawer.saveInstanceState(outState);
            //add the values which need to be saved from the accountHeader to the bundle
            outState = mAccountHeader.saveInstanceState(outState);
        }
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

    private void closeDrawer() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        }
    }

    public static Intent newInstance(final Context context) {
        return new Intent(context, MainActivity.class);
    }

}
