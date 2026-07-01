# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\kushs\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and flags by editing the proguardFiles
# attribute in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any custom rules here.

# JavaMail API Rules
-keep class javax.mail.** { *; }
-keep class javax.activation.** { *; }
-keep class com.sun.mail.** { *; }

# Suppress warnings for missing optional dependencies
-dontwarn javax.mail.**
-dontwarn javax.activation.**
-dontwarn java.awt.**
-dontwarn java.beans.Beans
-dontwarn javax.security.**

