# Android--Dota2-Mobile-Stats

Material Design Application for viewing Dota2 Match History and Steam Data using the Steam APIs.

## About

This was one of the larger personal projects as I needed to document the Steam APIs 
and properly map responses to errors, heroes, items and statuses, 
all the while conforming to the Steam Service limitations and providing a smooth experience to the user.

To that end, a combination of Android Architectural Components (which was released midway through this project_
was used along with EventBus and JobQueue. Apart from OkHttp caching, Room DB is also used for storing data until they
become stale. A wrapper was also written around SharedPreferences so they could be used with LiveData.

## Dependencies

- Steam API Library for Java - https://github.com/MikeFot/Java--Steam-Loader
- Android Lifecycle, Room and ViewModel
- EventBus
- Priority JobQueue (wrapper for conforming with Steam API service limitations)
- MPAndroid Chart
- Picasso
- Retrofit 2
- MikePenz Material Drawer and FontAwesome
- Dagger 2
- Butterknife
- LeakCanary
- Simple Tooltip
- Android Support, Design, Vector Drawable etc.
- Country Flags Library - https://github.com/MikeFot/Android--Library-Country-Flags
- Java Annotated Validator - https://github.com/MikeFot/java-lib-annotated-validator


## Play Store Page

<a href='https://play.google.com/store/apps/details?id=com.michaelfotiadis.dota2viewer&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_gb/badges/images/generic/en_badge_web_generic.png'/></a>
