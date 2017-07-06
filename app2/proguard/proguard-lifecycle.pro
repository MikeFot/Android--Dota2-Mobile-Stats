-keep public class android.arch.lifecycle.** {
    public protected *;
}

-keepclassmembers class ** {
    @android.arch.lifecycle.OnLifecycleEvent public *;
}

-keep public class android.arch.persistence.** {
    public protected *;
}

-keep public class android.arch.persistence.** {
    public protected *;
}



-keep public class android.arch.lifecycle.** {
    public protected *;
}


-keep public class android.arch.core.util.** {
    public protected *;
}


## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}
# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
