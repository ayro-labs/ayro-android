package io.chatz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.chatz.ChatzStatus;
import io.chatz.Settings;
import io.chatz.model.User;

public class Preferences {

  private static final String PREFERENCES_NAME = "chatz";
  private static final String SETTINGS = "settings";
  private static final String USER = "user";
  private static final String STATUS = "status";
  private static final String TOKEN = "token";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  public static Settings getSettings(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String settingsJson = preferences.getString(SETTINGS, null);
    return settingsJson != null ? JsonUtils.fromJson(settingsJson, Settings.class) : null;
  }

  public static void setSettings(Context context, Settings settings) {
    if(settings != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(SETTINGS, JsonUtils.toJson(settings)).apply();
    }
  }

  public static void removeSettings(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(SETTINGS).apply();
  }

  public static User getUser(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String userJson = preferences.getString(USER, null);
    return userJson != null ? JsonUtils.fromJson(userJson, User.class) : null;
  }

  public static void setUser(Context context, User user) {
    if(user != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(USER, JsonUtils.toJson(user)).apply();
    }
  }

  public static void removeUser(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(USER).apply();
  }

  public static ChatzStatus getStatus(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String status = preferences.getString(STATUS, null);
    return status != null ? ChatzStatus.valueOf(status) : ChatzStatus.NONE;
  }

  public static void setStatus(Context context, ChatzStatus status) {
    if(status != null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().putString(STATUS, status.toString()).apply();
    }
  }

  public static void removeStatus(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    preferences.edit().remove(STATUS).apply();
  }

  public static String getToken(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    return preferences.getString(TOKEN, null);
  }

  public static void setToken(Context context, String token) {
    if(token != null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().putString(TOKEN, token).apply();
    }
  }

  public static void removeToken(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    preferences.edit().remove(TOKEN).apply();
  }
}