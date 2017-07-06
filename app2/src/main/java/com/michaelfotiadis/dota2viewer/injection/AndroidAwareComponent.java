package com.michaelfotiadis.dota2viewer.injection;

import com.michaelfotiadis.dota2viewer.Dota2Application;
import com.michaelfotiadis.dota2viewer.data.loader.jobs.JobFactory;
import com.michaelfotiadis.dota2viewer.ui.activity.details.match.DotaMatchDetailsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.main.LoginFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.popular.PopularPlayersFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.result.PlayerPickerFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.login.fragment.web.WebViewFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.MainActivity;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.heroes.DotaHeroesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.hero.stats.DotaHeroAttributesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.items.DotaItemsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.econ.item.rarities.DotaRaritiesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.league.DotaLeagueListingsFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.live.DotaLiveGamesFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.dota.match.overview.DotaMatchOverviewFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.library.SteamLibraryFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.main.fragment.steam.user.profile.SteamProfileFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.calendar.CalendarFragment;
import com.michaelfotiadis.dota2viewer.ui.activity.performance.fragment.hero.HeroStatsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AndroidAwareComponent {

    void inject(LoginFragment loginFragment);

    void inject(DotaItemsFragment dotaItemsFragment);

    void inject(DotaRaritiesFragment dotaRaritiesFragment);

    void inject(DotaHeroesFragment dotaHeroesFragment);

    void inject(DotaHeroAttributesFragment dotaHeroStatsFragment);

    void inject(PlayerPickerFragment playerPickerFragment);

    void inject(MainActivity mainActivity);

    void inject(SteamProfileFragment steamUserFragment);

    void inject(SteamLibraryFragment steamLibraryFragment);

    void inject(DotaMatchOverviewFragment dotaMatchOverviewFragment);

    void inject(Dota2Application dota2Application);

    void inject(CalendarFragment calendarFragment);

    void inject(HeroStatsFragment heroStatsFragment);

    void inject(DotaMatchDetailsFragment dotaMatchDetailsFragment);

    void inject(DotaLeagueListingsFragment dotaLeagueListingsFragment);

    void inject(DotaLiveGamesFragment dotaLiveGamesFragment);

    void inject(JobFactory jobFactory);

    void inject(WebViewFragment webViewFragment);

    void inject(PopularPlayersFragment popularPlayersFragment);

}
