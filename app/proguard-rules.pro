-optimizationpasses 6
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes **

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.text.ParcelableSpan

-keepattributes *Annotation*,EnclosingMethod,InnerClasses

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    *;
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class android.support.**{
    *;
}

-dontwarn android.support.**
-dontwarn design.**
-dontwarn org.apache.**
-dontwarn com.facebook.**
-dontwarn okio.**
-dontwarn retrofit2.**


