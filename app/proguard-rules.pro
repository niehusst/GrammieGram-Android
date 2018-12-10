# Proguard rules for shrinking code
-dontobfuscate
#-ignorewarnings
#-dontoptimize
-dontpreverify

# Prints some helpful hints, always add this option
-verbose

# annotations not used at runtime
-dontwarn javax.annotation.**

# don't use kotlin
-dontwarn kotlin.**

# Specific classes that common test libs warn about
#-dontwarn java.beans.**
#-dontwarn javax.lang.model.element.Modifier
#-dontwarn org.apache.tools.ant.**
#-dontwarn org.assertj.core.internal.cglib.asm.util.TraceClassVisitor
#-dontwarn org.easymock.**
#-dontwarn org.jmock.core.**
#-dontwarn org.w3c.dom.bootstrap.**
#-dontwarn sun.misc.Unsafe
#-dontwarn sun.reflect.**
-libraryjars /Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/javafx-mx.jar

# add all known-to-be-safely-shrinkable classes to the beginning of line below
#-keepattributes SourceFile,LineNumberTable,Exceptions,InnerClasses,Signature,Deprecated,*Annotation*,EnclosingMethod

# OkHttp3
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

#Butterknife
#-keep public class * implements butterknife.Unbinder { public <init>(...); }
#-keep class butterknife.*
#-keepclasseswithmembernames class * { @butterknife.* <methods>; }
#-keepclasseswithmembernames class * { @butterknife.* <fields>; }

#Mockito
-dontwarn net.bytebuddy.**
-dontwarn org.mockito.**
-dontwarn org.objenesis.**
#-dontwarn org.mockito.internal.creation.bytebuddy.**

#Junit
#-dontwarn android.test.**

#Picasso
-dontwarn com.squareup.okhttp.**

#Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.dubai.fa.model.** { *; }
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer

# Retrofit 2.X
#-dontwarn retrofit2.**
-dontwarn org.jetbrains.annotations.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions
#-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }

# support v7
#-keep public class android.support.v7.widget.** { *; }
#-keep public class android.support.v7.internal.widget.** { *; }
#-keep public class android.support.v7.internal.view.menu.** { *; }
#-dontwarn android.support.v4.**

# Assertj
#-dontwarn org.assertj.core.**
#-dontwarn org.junit.**
#-dontwarn java.beans.**
#-dontwarn sun.reflect.**
#-dontwarn android.test.**
#-dontwarn net.bytebuddy.**
#-keep class java.beans.** { *; }
#-dontwarn retrofit.MockRestAdapter.**
#-dontwarn rx.**
#-dontwarn org.hamcrest.**

# Espresso
#-dontwarn android.support.test.**
#-keep class org.hamcrest.** { *; }
#-keep class org.junit.** { *; }
#-dontwarn org.junit.**
#-keep class junit.** { *; }
#-dontwarn junit.**
#-keep class sun.misc.** { *; }
#-dontwarn sun.misc.**
#-keepclassmembers class * { public void *test*(...); }