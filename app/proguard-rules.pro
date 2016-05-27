-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

## ----------------------------------
##      高德地图sdk混淆
## ----------------------------------
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


-dontwarn com.amap.api.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**
-dontwarn com.aps.**
-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}
#3D 地图
-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
#Service
-keep   class com.amap.api.services.**{*;}

#高德相关混淆文件
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#信鸽推送
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

#mob
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class m.framework.**{*;}
-keep class cn.smssdk.**{*;}
-keep class com.mob.**{*;}



-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}