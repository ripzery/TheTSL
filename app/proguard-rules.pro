## Add project specific ProGuard rules here.
## By default, the flags in this file are appended to flags specified
## in /Users/visit/sdk/tools/proguard/proguard-android.txt
## You can edit the include path and order by changing the proguardFiles
## directive in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## Add any project specific keep options here:
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interfaces
## class:
##-keepclassmembers class fqcn.of.javascript.interfaces.for.webview {
##   public *;
##}
#
#
##build.gradle
##
##    compile 'io.reactivex:rxandroid:1.0.1'
##    compile 'io.reactivex:rxjava:1.0.14'
##    compile 'io.reactivex:rxjava-math:1.0.0'
##    compile 'com.jakewharton.rxbinding:rxbinding:0.2.0'
#
## rxjava
#-keep class rx.schedulers.Schedulers {
#    public static <methods>;
#}
#-keep class rx.schedulers.ImmediateScheduler {
#    public <methods>;
#}
#-keep class rx.schedulers.TestScheduler {
#    public <methods>;
#}
#-keep class rx.schedulers.Schedulers {
#    public static ** test();
#}
#-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
#    long producerIndex;
#    long consumerIndex;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
#    long producerNode;
#    long consumerNode;
#}
#
## appcompat v7
#-keep public class android.support.v7.widget.** { *; }
#-keep public class android.support.v7.internal.widget.** { *; }
#-keep public class android.support.v7.internal.view.menu.** { *; }
#
#-keep public class * extends android.support.v4.view.ActionProvider {
#    public <init>(android.content.Context);
#}
#
##facebook
#
#-keep class com.facebook.** { *; }
#-keepattributes Signature
#
## Gson uses generic type information stored in a class file when working with fields. Proguard
## removes such information by default, so configure it to keep all of it.
#-keepattributes Signature
#
## For using GSON @Expose annotation
#-keepattributes *Annotation*
#
#-keepattributes EnclosingMethod
#
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#
#
## Glide specific rules #
## https://github.com/bumptech/glide
#
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
