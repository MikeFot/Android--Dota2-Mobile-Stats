package com.michaelfotiadis.dota2viewer.ui.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.michaelfotiadis.dota2viewer.R;
import com.michaelfotiadis.dota2viewer.data.persistence.db.model.PlayerEntity;
import com.michaelfotiadis.dota2viewer.utils.AppLog;
import com.michaelfotiadis.dota2viewer.utils.TextUtils;
import com.michaelfotiadis.steam.data.steam.users.user.PlayerSummary;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

public final class DrawerFactory {

    static long ID_STEAM = 1000L;
    static long ID_MATCHES = 2000L;
    static long ID_HEROES = 3000L;
    static long ID_ITEMS = 4000L;

    static final int PROFILE_ADD = 100000;

    @NonNull
    private final Activity mActivity;
    @Nullable
    private final Bundle mSavedInstanceState;
    @Nullable
    private final NavigationListener mNavigationListener;

    DrawerFactory(@NonNull final Activity activity,
                  @Nullable final Bundle savedInstanceState,
                  @Nullable final NavigationListener navigationListener) {
        mActivity = activity;
        mSavedInstanceState = savedInstanceState;
        mNavigationListener = navigationListener;
    }

    protected DrawerBuilder getDrawer(final Toolbar toolbar) {

        final DrawerBuilder builder = new DrawerBuilder();
        builder.withActivity(mActivity);
        builder.withToolbar(toolbar);
        builder.withTranslucentStatusBar(true);
        //builder.withCloseOnClick(true);
        if (mSavedInstanceState != null) {
            builder.withSavedInstance(mSavedInstanceState);
        }

        final SecondaryDrawerItem title1 = new SecondaryDrawerItem()
                .withName(R.string.drawer_title_steam)
                .withSelectable(false);

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withName(R.string.drawer_title_user)
                .withIdentifier(ID_STEAM)
                .withIcon(FontAwesome.Icon.faw_steam)
                .withSelectable(true);

        final SecondaryDrawerItem title2 = new SecondaryDrawerItem()
                .withName(R.string.drawer_title_dota)
                .withSelectable(false);

        final PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withName(R.string.drawer_title_matches)
                .withIdentifier(ID_MATCHES)
                .withIcon(FontAwesome.Icon.faw_gamepad)
                .withSelectable(true);
        final PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withName(R.string.drawer_title_heroes)
                .withIdentifier(ID_HEROES)
                .withIcon(FontAwesome.Icon.faw_list)
                .withSelectable(true);
        final PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withName(R.string.drawer_title_items)
                .withIdentifier(ID_ITEMS)
                .withIcon(FontAwesome.Icon.faw_italic)
                .withSelectable(true);

        builder.addDrawerItems(
                title1,
                item1,
                new DividerDrawerItem(),
                title2,
                item2,
                item3,
                item4);

        if (mNavigationListener != null) {
            builder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(final View view, final int position, final IDrawerItem drawerItem) {

                    final long id = drawerItem.getIdentifier();
                    AppLog.d("On item click: " + id);
                    if (DrawerFactory.ID_STEAM == id) {
                        mNavigationListener.onSteamUserSelected();
                        return true;
                    } else if (DrawerFactory.ID_MATCHES == id) {
                        mNavigationListener.onDotaMatchesSelected();
                        return true;
                    } else if (DrawerFactory.ID_HEROES == id) {
                        mNavigationListener.onDotaEconHeroesSelected();
                        return true;
                    } else if (DrawerFactory.ID_ITEMS == id) {
                        mNavigationListener.onDotaEconItemsSelected();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        return builder;
    }

    protected AccountHeader getAccountHeader(final List<PlayerEntity> playerEntities,
                                             @Nullable final String currentUserId) {
        // Create the AccountHeader
        final AccountHeaderBuilder builder = new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.pattern_5)
                .withCloseDrawerOnProfileListClick(false);

        if (mSavedInstanceState != null) {
            builder.withSavedInstance(mSavedInstanceState);
        }

        IProfile currentProfile = null;
        if (playerEntities.isEmpty()) {
            final IProfile addUserProfile = new ProfileDrawerItem()
                    .withName(R.string.account_header_add_title)
                    .withEmail(R.string.account_header_add_email)
                    .withIdentifier(PROFILE_ADD);
            builder.addProfiles(addUserProfile);
        } else {


            for (final PlayerEntity entity : playerEntities) {
                if (entity != null && TextUtils.isNotEmpty(entity.getId())) {
                    final IProfile iProfile = getProfile(entity.getPlayerSummary());
                    builder.addProfiles(iProfile);
                    if (TextUtils.isNotEmpty(currentUserId) && entity.getId().equals(currentUserId)) {
                        currentProfile = iProfile;
                    }
                }
            }

            builder.addProfiles(new ProfileSettingDrawerItem()
                    .withName("Add Account")
                    .withDescription("Add new Steam Account")
                    .withIcon(new IconicsDrawable(mActivity, FontAwesome.Icon.faw_plus)
                            .actionBar()
                            .paddingDp(5)
                            .colorRes(R.color.primary_text))
                    .withIdentifier(PROFILE_ADD)
            );
        }

        if (mNavigationListener != null) {
            builder.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(final View view, final IProfile profile, final boolean current) {
                    AppLog.d("Profile changed " + profile.getIdentifier() + " current " + current);
                    if (profile.getIdentifier() == PROFILE_ADD) {
                        mNavigationListener.onAddProfile(view);
                        return true;
                    } else if (!current) {
                        mNavigationListener.onProfileSelected(view, profile.getIdentifier());
                    }

                    return false;
                }
            });
        }

        final AccountHeader accountHeader = builder.build();
        if (currentProfile != null) {
            accountHeader.setActiveProfile(currentProfile);
        }

        return accountHeader;
    }

    private static IProfile getProfile(final PlayerSummary playerSummary) {
        return new ProfileDrawerItem()
                .withName(playerSummary.getSteamId())
                .withIdentifier(Long.parseLong(playerSummary.getSteamId()))
                .withIcon(playerSummary.getAvatarMedium())
                .withEmail(playerSummary.getPersonaName());
    }


}
