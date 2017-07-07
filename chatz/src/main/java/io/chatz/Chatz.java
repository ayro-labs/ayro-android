package io.chatz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.chatz.exception.ChatzException;
import io.chatz.model.User;
import io.chatz.service.ChatzService;
import io.chatz.service.Services;
import io.chatz.service.payload.LoginResult;
import io.chatz.task.impl.FirebaseConnectionTask;
import io.chatz.task.impl.LoginTask;
import io.chatz.task.Tasks;
import io.chatz.task.impl.UpdateUserTask;
import io.chatz.ui.activity.ChatzActivity;
import io.chatz.util.AppUtils;
import io.chatz.util.Callback;
import io.chatz.util.Constants;
import io.chatz.util.Preferences;

public class Chatz {

  private static Chatz instance;

  private Context context;
  private Settings settings;
  private Status status;
  private User user;
  private String apiToken;
  private boolean chatOpened;

  private Chatz(Context context) {
    this.context = context;
    this.settings = Preferences.getSettings(context);
    this.user = Preferences.getUser(context);
    this.status = Preferences.getStatus(context);
    this.apiToken = Preferences.getApiToken(context);
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

  public Status getStatus() {
    return status;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void init(Settings settings) {
    clearApp();
    saveSettings(settings);
    if(Status.NOT_INITIALIZED.equals(status)) {
      saveStatus(Status.INITIALIZED);
    }
    Log.d(Constants.TAG, "ChatzIO was initialized");
  }

  public void login() {
    this.login(new User());
  }

  public void login(User user) {
    assertInitialized();
    saveUser(user);
    Log.d(Constants.TAG, "Authenticating user...");
    LoginTask task = new LoginTask(settings.getAppToken(), user, AppUtils.getDevice(context));
    Tasks.execute(task, new Callback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult result) {
        Log.d(Constants.TAG, "User was authenticated with success");
        saveUser(result.getUser());
        saveApiToken(result.getToken());
        saveStatus(Status.LOGGED_IN);
        connectToFirebase();
      }
    });
  }

  public void connectToFirebase() {
    Log.d(Constants.TAG, "Connecting user to Firebase...");
    FirebaseConnectionTask task = new FirebaseConnectionTask(context);
    Tasks.execute(task, new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        Log.d(Constants.TAG, "User was connected to Firebase with success");
      }
    });
  }

  public void logout() {
    assertInitialized();
    saveStatus(Status.LOGGED_OUT);
    clearApp();
    Log.d(Constants.TAG, "User was logged out with success");
  }

  public void updateUser(User user) {
    assertInitialized();
    saveUser(user);
    Log.d(Constants.TAG, "Updating user...");
    UpdateUserTask task = new UpdateUserTask(context, user);
    Tasks.execute(task, new Callback<Void>() {
      @Override
      public void onSuccess(Void result) {
        Log.d(Constants.TAG, "User was updated with success");
      }
    });
  }

  public void openChat() {
    assertInitialized();
    if(!Status.LOGGED_IN.equals(status)) {
      login();
    }
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
    if(status.getOrder() < Status.INITIALIZED.getOrder()) {
      throw new ChatzException("ChatzIO was not initialized previously. Please call init method first.");
    }
  }

  private void saveSettings(Settings settings) {
    this.settings = settings;
    Preferences.setSettings(context, settings);
  }

  private void saveUser(User user) {
    this.user = user;
    Preferences.setUser(context, user);
  }

  private void saveStatus(Status status) {
    this.status = status;
    Preferences.setStatus(context, status);
  }

  private void saveApiToken(String apiToken) {
    Chatz.this.apiToken = apiToken;
    Preferences.setApiToken(context, apiToken);
  }

  private void clearApp() {
    Tasks.cancelAll();
    Preferences.removeSettings(context);
    Preferences.removeUser(context);
    Preferences.removeStatus(context);
    Preferences.removeApiToken(context);
  }
}