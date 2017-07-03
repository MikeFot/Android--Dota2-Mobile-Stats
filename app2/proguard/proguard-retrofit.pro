# Retrofit 1.X

-keep,includedescriptorclasses class com.squareup.okhttp.** { *; }
-keep,includedescriptorclasses class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn org.codehaus.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep public interface retrofit.Callback {*;}

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.


