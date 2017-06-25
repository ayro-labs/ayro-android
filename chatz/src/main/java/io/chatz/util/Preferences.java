package io.chatz.util;

import android.content.Context;
import android.content.SharedPreferences;

import io.chatz.Status;
import io.chatz.Settings;
import io.chatz.model.User;

public class Preferences {

  private static final String PREFERENCES_NAME = "chatz";
  private static final String SETTINGS = "settings";
  private static final String USER = "user";
  private static final String STATUS = "status";
  private static final String API_TOKEN = "api_token";

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

  public static Status getStatus(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String status = preferences.getString(STATUS, null);
    return status != null ? Status.valueOf(status) : Status.NOT_INITIALIZED;
  }

  public static void setStatus(Context context, Status status) {
    if(status != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(STATUS, status.toString()).apply();
    }
  }

  public static void removeStatus(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(STATUS).apply();
  }

  public static String getApiToken(Context context) {
    SharedPreferences preferences = getPreferences(context);
    return preferences.getString(API_TOKEN, null);
  }

  public static void setApiToken(Context context, String token) {
    if(token != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(API_TOKEN, token).apply();
    }
  }

  public static void removeApiToken(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(API_TOKEN).apply();
  }
}