

-keepattributes InnerClasses

-keep class **.R
-keep class **.R$* {
    <fields>;
}

-keep class com.michaelfotiadis.dota2viewer.ui.view.** { *; }
-keep class com.michaelfotiadis.dota2viewer.data.loader.** { *; }
-keep class com.michaelfotiadis.dota2viewer.event.** { *; }
-keep class com.michaelfotiadis.dota2viewer.data.persistence.** { *; }
-keep class com.michaelfotiadis.steam.** { *; }
-keep public interface com.michaelfotiadis.dota2viewer.event.Event {*;}
-keep class * implements com.michaelfotiadis.dota2viewer.event.Event


-keep public interface com.michaelfotiadis.dota2viewer.network.NetworkResolver {*;}
-keep public interface com.michaelfotiadis.dota2viewer.network.api.HeroStatsApi {*;}


-keepattributes *Annotation*
-keep class com.google.inject.** { *; } 
-keep class javax.inject.** { *; } 
-keep class javax.annotation.** { *; } 
-keep class roboguice.** { *; } 



