package io.chatz;

import android.content.Context;
import android.util.Log;

import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.task.FirebaseConnectionTask;
import io.chatz.task.LoginTask;
import io.chatz.task.Task;
import io.chatz.util.Constants;
import io.chatz.util.Preferences;

public class Chatz {

  private static final String PLATFORM = "android";

  private static Chatz instance;

  private Context context;
  private Settings settings;
  private User user;
  private ChatzStatus status;

  private Chatz(Context context) {
    this.context = context;
    this.settings = Preferences.getSettings(context);
    this.user = Preferences.getUser(context);
    this.status = Preferences.getStatus(context);
  }

  public static Chatz getInstance(Context context) {
    if(instance == null) {
      instance = new Chatz(context);
    }
    return instance;
  }

  public Settings getSettings() {
    return settings;
  }

  public void init(Settings settings) {
    this.settings = settings;
    Preferences.setSettings(context, settings);
    updateStatus(ChatzStatus.INITIALIZED);
    Log.d(Constants.TAG, "ChatzIO was initialized");
  }

  public void login() {
    this.login(null);
  }

  public void login(User user) {
    assertInitialized();
    Preferences.setUser(context, user);
    Log.d(Constants.TAG, "Authenticating user...");
    LoginTask task = new LoginTask(settings.getProjectToken(), user, getDevice());
    task.exec(new Task.TaskCallback<String>() {
      @Override
      public void onSuccess(String token) {
        updateStatus(ChatzStatus.LOGGED_IN);
        Preferences.setToken(context, token);
        Log.d(Constants.TAG, "User was authenticated with success");
        connectFirebase();
      }
    });
  }

  public void connectFirebase() {
    Log.d(Constants.TAG, "Connecting user to Firebase...");
    FirebaseConnectionTask task = new FirebaseConnectionTask();
    task.exec(new Task.TaskCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        Log.d(Constants.TAG, "User was connected to Firebase with success");
      }
    });
  }

  public void logout() {
    assertInitialized();
    updateStatus(ChatzStatus.LOGGED_OUT);
    Log.d(Constants.TAG, "User was logged out with with success");
  }

  public void updateUser(User user) {
    assertInitialized();
  }

  public void openChat() {
    assertInitialized();
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

  private void updateStatus(ChatzStatus status) {
    this.status = status;
    Preferences.setStatus(context, status);
  }
}