#######################
### Project sources ###
#######################

-keep class io.chatz.Chatz { *; }
-keep class io.chatz.ChatzMessages { *; }
-keep class io.chatz.Settings { *; }
-keep class io.chatz.User { *; }

-keepclassmembers class io.chatz.model.** implements android.os.Parcelable {
  static ** CREATOR;
}
-keepclassmembers class io.chatz.model.** implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  !static !transient <fields>;
  !private <fields>;
  !private <methods>;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

-keepattributes Signature
-keepattributes Annotation
-keepattributes InnerClasses
-keepattributes Exceptions

###########################
### Direct dependencies ###
###########################

# Gson
-keep class sun.misc.Unsafe { *; }

# Retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8

#############################
### Indirect dependencies ###
#############################

# OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# OkHttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Okio
-dontwarn okio.**