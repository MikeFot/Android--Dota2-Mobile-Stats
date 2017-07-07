

-keepattributes InnerClasses

-keep class **.R
-keep class **.R$* {
    <fields>;
}

-keep class com.michaelfotiadis.mobiledota2.ui.view.** { *; }
-keep class com.michaelfotiadis.mobiledota2.data.loader.** { *; }
-keep class com.michaelfotiadis.mobiledota2.event.** { *; }
-keep class com.michaelfotiadis.mobiledota2.data.persistence.** { *; }
-keep class com.michaelfotiadis.steam.** { *; }
-keep class com.michaelfotiadis.mobiledota2.event.listener.EventLifecycleListener { *; }
-keep public interface com.michaelfotiadis.mobiledota2.event.Event {*;}
-keep public class * implements com.michaelfotiadis.mobiledota2.event.Event
-keep public class * extends com.michaelfotiadis.mobiledota2.data.loader.jobs.BaseJob
-keep public class * extends android.arch.lifecycle.ViewModel
-keep public class * extends android.arch.lifecycle.Lifecycle


-keep public interface com.michaelfotiadis.mobiledota2.network.NetworkResolver {*;}
-keep public interface com.michaelfotiadis.mobiledota2.network.api.HeroStatsApi {*;}


-keepattributes *Annotation*
-keep class com.google.inject.** { *; } 
-keep class javax.inject.** { *; } 
-keep class javax.annotation.** { *; } 
-keep class roboguice.** { *; } 



