package io.chatz.store;

import android.content.Context;
import android.content.SharedPreferences;

import io.chatz.enums.AppStatus;
import io.chatz.Settings;
import io.chatz.enums.UserStatus;
import io.chatz.model.App;
import io.chatz.model.Integration;
import io.chatz.model.User;
import io.chatz.util.JsonUtils;

public class Store {

  private static final String PREFERENCES_NAME = "chatz";
  private static final String APP_STATUS = "app_status";
  private static final String USER_STATUS = "user_status";
  private static final String SETTINGS = "settings";
  private static final String APP = "app";
  private static final String INTEGRATION = "integration";
  private static final String USER = "user";
  private static final String API_TOKEN = "api_token";
  private static final String DEVICE_ID = "device_id";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  public static AppStatus getAppStatus(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String status = preferences.getString(APP_STATUS, null);
    return status != null ? AppStatus.valueOf(status) : AppStatus.NOT_INITIALIZED;
  }

  public static void setAppStatus(Context context, AppStatus appStatus) {
    if (appStatus != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(APP_STATUS, appStatus.toString()).apply();
    }
  }

  public static UserStatus getUserStatus(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String status = preferences.getString(USER_STATUS, null);
    return status != null ? UserStatus.valueOf(status) : UserStatus.LOGGED_OUT;
  }

  public static void setUserStatus(Context context, UserStatus appStatus) {
    if (appStatus != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(USER_STATUS, appStatus.toString()).apply();
    }
  }

  public static Settings getSettings(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String settingsJson = preferences.getString(SETTINGS, null);
    return settingsJson != null ? JsonUtils.fromJson(settingsJson, Settings.class) : null;
  }

  public static void setSettings(Context context, Settings settings) {
    if (settings != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(SETTINGS, JsonUtils.toJson(settings)).apply();
    }
  }

  public static App getApp(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String userJson = preferences.getString(APP, null);
    return userJson != null ? JsonUtils.fromJson(userJson, App.class) : null;
  }

  public static void setApp(Context context, App user) {
    if (user != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(APP, JsonUtils.toJson(user)).apply();
    }
  }

  public static Integration getIntegration(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String userJson = preferences.getString(INTEGRATION, null);
    return userJson != null ? JsonUtils.fromJson(userJson, Integration.class) : null;
  }

  public static void setIntegration(Context context, Integration user) {
    if (user != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(INTEGRATION, JsonUtils.toJson(user)).apply();
    }
  }

  public static User getUser(Context context) {
    SharedPreferences preferences = getPreferences(context);
    String userJson = preferences.getString(USER, null);
    return userJson != null ? JsonUtils.fromJson(userJson, User.class) : null;
  }

  public static void setUser(Context context, User user) {
    if (user != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(USER, JsonUtils.toJson(user)).apply();
    }
  }

  public static void unsetUser(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(USER).apply();
  }

  public static String getApiToken(Context context) {
    SharedPreferences preferences = getPreferences(context);
    return preferences.getString(API_TOKEN, null);
  }

  public static void setApiToken(Context context, String token) {
    if (token != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(API_TOKEN, token).apply();
    }
  }

  public static void unsetApiToken(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(API_TOKEN).apply();
  }

  public static String getDeviceId(Context context) {
    SharedPreferences preferences = getPreferences(context);
    return preferences.getString(DEVICE_ID, null);
  }

  public static void setDeviceId(Context context, String deviceId) {
    if (deviceId != null) {
      SharedPreferences preferences = getPreferences(context);
      preferences.edit().putString(DEVICE_ID, deviceId).apply();
    }
  }

  public static void clear(Context context) {
    SharedPreferences preferences = getPreferences(context);
    preferences.edit().remove(APP_STATUS).remove(USER_STATUS).remove(SETTINGS).remove(APP).remove(INTEGRATION).remove(USER).remove(API_TOKEN).remove(DEVICE_ID).apply();
  }
}