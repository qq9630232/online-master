# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
#-ignorewarnings
#
#-keepattributes Signature,*Annotation*
#
## keep BmobSDK
#-dontwarn cn.bmob.v3.**
#-keep class cn.bmob.v3.** {*;}
#
## 确保JavaBean不被混淆-否则gson将无法将数据解析成具体对象
#-keep class * extends cn.bmob.v3.BmobObject {
#    *;
#}
#-keep class com.example.bmobexample.bean.BankCard{*;}
#-keep class com.example.bmobexample.bean.GameScore{*;}
#-keep class com.example.bmobexample.bean.MyUser{*;}
#-keep class com.example.bmobexample.bean.Person{*;}
#-keep class com.example.bmobexample.file.Movie{*;}
#-keep class com.example.bmobexample.file.Song{*;}
#-keep class com.example.bmobexample.relation.Post{*;}
#-keep class com.example.bmobexample.relation.Comment{*;}
#
## keep BmobPush
#-dontwarn  cn.bmob.push.**
#-keep class cn.bmob.push.** {*;}
#
## keep okhttp3、okio
#-dontwarn okhttp3.**
#-keep class okhttp3.** { *;}
#-keep interface okhttp3.** { *; }
#-dontwarn okio.**
#
## keep rx
#-dontwarn sun.misc.**
#-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
# long producerIndex;
# long consumerIndex;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode producerNode;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}
#
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
#-dontshrink
#-allowaccessmodification
#-keepattributes *Annotation*
#-keepattributes Exceptions
#-keepattributes JavascriptInterface
#-keepattributes LineNumberTable
#-keepattributes Signature
#-keepattributes SourceFile
#
#
##想要混淆代码一定要开启在project.properties里卖弄的 #Project target里面的#去掉
##三方jar包 全部都填上 填写完毕 一定要看指向 并且要开启混淆代码 idea 需要打上勾(在打包的时候)RUn..
#
#-keepdirectories
##忽略 可以看警告 混淆代码时候
#-dontwarn android.support.**
#-dontwarn **CompatHoneycomb
#-dontwarn **CompatHoneycombMR2
#-dontwarn **CompatCreatorHoneycombMR2
#-dontwarn com.squareup.**
#
##javax
#-dontwarn javax.**
#
#-dontwarn com.baidu.**
#
#-dontwarn com.bumptech.glide.**
#
##glide如果你的API级别<=Android API 27 则需要添加
#-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
##libs里面的jar包 里面的包一定要对应上 不对应上 崩溃
### v4,v7,v13包
#-keep class android.support.** {*;}
#
##javax
#-keep class javax.**{*;}
#
##okhttp
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
#-dontwarn okhttp3.**
# #okio
#
#-keep class okio.** { *; }
#-keep interface okio.** { *; }
#-dontwarn okio.**
#
#
#-keep class com.baidu.**{*;}
#
#
#-keep class com.yiche.elita_lib.model.**{ *; }
#
##保留sdk 使用初始化
#-keep class com.yiche.elita_lib.sdk.**{ *; }
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
## glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
#
#
##保持继承的不被混淆
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#-keep public class * extends android.app.Fragment
#
##关于反射 泛型 如果没有它 崩溃
#-keep class sun.misc.Unsafe { *; }
#
#-keep public class com.yiche.elita_lib.ui.camera.ElitaFaceActivity
#
##keep all classes that might be used in XML layouts
#-keep public class * extends android.view.View
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.support.v4.Fragment
#-keep public class * extends java.lang.annotation.Annotation
#
#
#-keepclasseswithmembers class * {
#    native <methods>;
#}
##保持2个参数的构造方法
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
##保持3个构造方法
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
## 同上
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
#rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}
#
##-ButterKnife 7.0
# -keep class butterknife.** { *; }
# -dontwarn butterknife.internal.**
# -keep class **$$ViewBinder { *; }
# -keepclasseswithmembernames class * {
#  @butterknife.* <fields>;
# }
# -keepclasseswithmembernames class * {
# @butterknife.* <methods>;
# }
#-keep public class * extends android.view.View{*;}
#-keep public class * implements com.kk.taurus.playerbase.player.IPlayer{*;}
