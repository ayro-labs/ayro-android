package io.chatz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.chatz.exception.ChatzException;
import io.chatz.model.ChatMessage;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import io.chatz.task.impl.FirebaseConnectionTask;
import io.chatz.task.impl.LoginTask;
import io.chatz.task.Tasks;
import io.chatz.task.impl.UpdateUserTask;
import io.chatz.ui.activity.ChatzActivity;
import io.chatz.util.AppUtils;
import io.chatz.util.Callback;
import io.chatz.util.Constants;
import io.chatz.util.Preferences;
import retrofit2.Call;

public class Chatz {

  private static Chatz instance;

  private Context context;
  private Settings settings;
  private ChatzStatus status;
  private User user;
  private String apiToken;
  private boolean chatOpened;
  private ApiService apiService;

  private Chatz(Context context) {
    this.context = context;
    this.settings = Preferences.getSettings(context);
    this.user = Preferences.getUser(context);
    this.status = Preferences.getStatus(context);
    this.apiService = Services.getInstance().getApiService();
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

  public User getUser() {
    return user;
  }

  public ChatzStatus getStatus() {
    return status;
  }

  public void init(Settings settings) {
    clearApp();
    this.settings = settings;
    Preferences.setSettings(context, settings);
    if(ChatzStatus.NONE.equals(status)) {
      updateStatus(ChatzStatus.INITIALIZED);
    }
    Log.d(Constants.TAG, "ChatzIO was initialized");
  }

  public void login() {
    this.login(null);
  }

  public void login(User user) {
    assertInitialized();
    this.user = user;
    Preferences.setUser(context, user);
    Log.d(Constants.TAG, "Authenticating user...");
    LoginTask task = new LoginTask(settings.getProjectToken(), user, getDevice());
    Tasks.execute(task, new Callback<String>() {
      @Override
      public void onSuccess(String apiToken) {
        Chatz.this.apiToken = apiToken;
        Preferences.setApiToken(context, apiToken);
        updateStatus(ChatzStatus.LOGGED_IN);
        Log.d(Constants.TAG, "User was authenticated with success");
        connectToFirebase();
      }
    });
  }

  public void connectToFirebase() {
    Log.d(Constants.TAG, "Connecting user to Firebase...");
    FirebaseConnectionTask task = new FirebaseConnectionTask(context, apiToken);
    Tasks.execute(task, new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        Log.d(Constants.TAG, "User was connected to Firebase with success");
      }
    });
  }

  public void logout() {
    assertInitialized();
    clearApp();
    updateStatus(ChatzStatus.LOGGED_OUT);
    Log.d(Constants.TAG, "User was logged out with with success");
  }

  public void updateUser(User user) {
    assertLoggedIn();
    this.user = user;
    Preferences.setUser(context, user);
    Log.d(Constants.TAG, "Updating user...");
    UpdateUserTask task = new UpdateUserTask(apiToken, user);
    Tasks.execute(task, new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        Log.d(Constants.TAG, "User was updated with success");
      }
    });
  }

  public void openChat() {
    Intent intent = new Intent(context, ChatzActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  public boolean isChatOpened() {
    return chatOpened;
  }

  public void setChatOpened(boolean chatOpened) {
    this.chatOpened = chatOpened;
  }

  private void assertInitialized() {
    if(!ChatzStatus.INITIALIZED.equals(status)) {
      throw new ChatzException("ChatzIO was not initialized previously. Please call init method first.");
    }
  }

  private void assertLoggedIn() {
    if(!ChatzStatus.LOGGED_IN.equals(status)) {
      throw new ChatzException("User was not logged in previously. Please call login method first.");
    }
  }

  private Device getDevice() {
    Device device = new Device();
    device.setUid(AppUtils.getDeviceId(context));
    device.setAppId(BuildConfig.APPLICATION_ID);
    device.setAppVersion(BuildConfig.VERSION_NAME);
    device.setPlatform(Constants.PLATFORM);
    return device;
  }

  private void updateStatus(ChatzStatus status) {
    this.status = status;
    Preferences.setStatus(context, status);
  }

  private void clearApp() {
    Tasks.cancelAll();
    Preferences.removeSettings(context);
    Preferences.removeUser(context);
    Preferences.removeStatus(context);
    Preferences.removeToken(context);
  }

  public Call<Void> postMessage(ChatMessage chatMessage) {
    return apiService.postMessage(apiToken, chatMessage);
  }
}