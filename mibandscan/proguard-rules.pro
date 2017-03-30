-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService


#这是友盟社会化组件和统计提供的混淆代码--end
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


-dontwarn com.umeng.**

-dontwarn org.apache.commons.**

-dontwarn com.tencent.weibo.sdk.**

-keepattributes *Annotation*

-keep class com.umeng*.** {*; }

-keep public class [your_pkg].R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*

-keep class com.tencent.open.TDialog$* {*;}

-keep class com.tencent.open.PKDialog

-keep class com.tencent.open.PKDialog {*;}

-keep class com.tencent.open.PKDialog$*

-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep public class com.huami.mibandscan.MiBandScan {
	public <fields>;
	public <methods>;
}

-keep public class com.huami.mibandscan.MiBandScanCallBack {
	public <fields>;
	public <methods>;
}

-keep public class com.huami.mibandscan.MiBandScanResult {
	public <fields>;
	public <methods>;
}

-keep public class com.huami.mibandscan.MiBandScanStatus {*;}

-keep public class com.huami.mibandscan.FormBean.** {*;}