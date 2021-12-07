# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep public class com.umer.application.activities.SplashActivity
-keep public class com.umer.application.activities.GridViewActivity
-keep public class com.umer.application.activities.DailyMotionVideoPlayer
-keep public class com.umer.application.activities.WebViewActivity
-keep public class com.umer.application.activities.YoutubeVideoPlayer
-keep public class com.umer.application.fragments.VideoListFragment
-keep public class com.umer.application.adapters.VideoListAdapter
-keep public class com.umer.application.adapters.GridViewAdapter
-keep public class com.umer.application.adapters.imagesAdapter
-keep public class com.umer.application.adapters.SliderAdapter
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName

-dontwarn kotlinx.atomicfu.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }