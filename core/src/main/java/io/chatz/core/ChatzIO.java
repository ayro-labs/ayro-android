package io.chatz.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.chatz.BuildConfig;
import io.chatz.core.activity.ConversationActivity;
import io.chatz.core.model.Device;
import io.chatz.core.model.User;

public class ChatzIO {

  private static final String PLATFORM = "android";

  private static ChatzIO instance;

  private Context context;
  private Settings settings;
  private User user;
  private Device device;

  private ChatzStatus status;

  private ChatzIO(Context context) {
    this.context = context;
    this.status = ChatzStatus.NONE;
  }

  public static ChatzIO getInstance(Context context) {
    if(instance == null) {
      instance = new ChatzIO(context);
    }
    return instance;
  }

  public void init(Settings settings) {
    this.settings = settings;
    this.status = ChatzStatus.INITIALIZED;
    Log.d(Constants.TAG, "ChatzIO was initialized");
  }

  public void login() {
    this.login(null);
  }

  public void login(User user) {
    assertInitialized();
    this.user = user;
    this.device = getDevice();
    Log.d(Constants.TAG, "Authenticating user...");
    new LoginTask().login(settings.getProjectToken(), user, device).exec(new LoginTask.LoginCallback() {
      @Override
      public void onLoggedIn(String token) {
        ChatzIO.this.status = ChatzStatus.LOGGED_IN;
        Log.d(Constants.TAG, "User was authenticated with success");
      }
    });
  }

  public void logout() {
    assertInitialized();
    this.user = null;
    this.status = ChatzStatus.LOGGED_OUT;
    Log.d(Constants.TAG, "User was logged out with with success");
  }

  public User getUser() {
    assertInitialized();
    return user;
  }

  public void updateUser() {
    assertInitialized();
  }

  public void openChat() {
    assertInitialized();
    context.startActivity(new Intent(context, ConversationActivity.class));
  }

  private void assertInitialized() {
    if(!ChatzStatus.INITIALIZED.equals(status)) {
      throw new RuntimeException("ChatzIO was not initialized previously. Please call init method first.");
    }
  }

  private Device getDevice() {
    Device device = new Device();
    device.setUid(android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
    device.setAppId(BuildConfig.APPLICATION_ID);
    device.setAppVersion(BuildConfig.VERSION_NAME);
    device.setPlatform(PLATFORM);
    return device;
  }
}