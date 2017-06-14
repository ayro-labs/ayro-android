package io.chatz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.chatz.ChatzStatus;
import io.chatz.Settings;
import io.chatz.model.User;

public class Preferences {

  private static final String SETTINGS = "chatz:settings";
  private static final String USER = "chatz:user";
  private static final String STATUS = "chatz:status";
  private static final String TOKEN = "chatz:token";

  private static Gson gson = createGson();

  private static Gson createGson() {
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  }

  public static Settings getSettings(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String settingsJson = preferences.getString(USER, null);
    return settingsJson != null ? gson.fromJson(settingsJson, Settings.class) : null;
  }

  public static void setSettings(Context context, Settings settings) {
    if(settings != null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().putString(SETTINGS, gson.toJson(settings)).apply();
    }
  }

  public static User getUser(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String userJson = preferences.getString(USER, null);
    return userJson != null ? gson.fromJson(userJson, User.class) : null;
  }

  public static void setUser(Context context, User user) {
    if(user != null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().putString(SETTINGS, gson.toJson(user)).apply();
    }
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
}