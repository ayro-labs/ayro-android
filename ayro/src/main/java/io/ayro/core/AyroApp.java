package io.ayro.core;

import android.content.Context;
import android.content.Intent;

import io.ayro.Settings;
import io.ayro.enums.AppStatus;
import io.ayro.enums.UserStatus;
import io.ayro.model.App;
import io.ayro.model.Integration;
import io.ayro.model.User;
import io.ayro.service.payload.InitResult;
import io.ayro.service.payload.LoginResult;
import io.ayro.service.payload.LogoutResult;
import io.ayro.store.Store;
import io.ayro.task.TaskCallback;
import io.ayro.task.TaskManager;
import io.ayro.task.impl.InitTask;
import io.ayro.task.impl.LoginTask;
import io.ayro.task.impl.LogoutTask;
import io.ayro.task.impl.UpdatePushTokenTask;
import io.ayro.task.impl.UpdateUserTask;
import io.ayro.ui.activity.ChatActivity;
import io.ayro.util.AppUtils;

public class AyroApp {

  private static AyroApp instance;

  private Context context;
  private AppStatus appStatus;
  private UserStatus userStatus;
  private Settings settings;
  private App app;
  private Integration integration;
  private User user;
  private String apiToken;
  private boolean chatOpened;

  private AyroApp(Context context) {
    this.context = context;
    this.appStatus = Store.getAppStatus(context);
    this.userStatus = Store.getUserStatus(context);
    this.settings = Store.getSettings(context);
    this.app = Store.getApp(context);
    this.integration = Store.getIntegration(context);
    this.user = Store.getUser(context);
    this.apiToken = Store.getApiToken(context);
  }

  public static AyroApp getInstance(Context context) {
    if (instance == null) {
      instance = new AyroApp(context);
    }
    return instance;
  }

  public AppStatus getAppStatus() {
    return appStatus;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public Settings getSettings() {
    return settings;
  }

  public App getApp() {
    return app;
  }

  public Integration getIntegration() {
    return integration;
  }

  public User getUser() {
    return user;
  }

  public String getApiToken() {
    return apiToken;
  }

  public void init(Settings settings) {
    setSettings(settings);
    InitTask task = new InitTask(context, settings.getAppToken(), AppUtils.getDevice(context));
    task.setCallback(new TaskCallback<InitResult>() {
      @Override
      public void onSuccess(InitResult result) {
        setAppStatus(AppStatus.INITIALIZED);
        setApp(result.getApp());
        setIntegration(result.getIntegration());
        setUser(result.getUser());
        setApiToken(result.getToken());
      }
    });
    task.schedule();
  }

  public void login(io.ayro.User user) {
    User ayroUser = AppUtils.getUser(context, user);
    LoginTask task = new LoginTask(context, settings.getAppToken(), ayroUser, AppUtils.getDevice(context));
    task.setCallback(new TaskCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult result) {
        setUserStatus(UserStatus.LOGGED_IN);
        setUser(result.getUser());
        setApiToken(result.getToken());
      }
    });
    task.schedule();
  }

  public void logout() {
    LogoutTask task = new LogoutTask(context);
    task.setCallback(new TaskCallback<LogoutResult>() {
      @Override
      public void onSuccess(LogoutResult result) {
        setUserStatus(UserStatus.LOGGED_OUT);
        setUser(result.getUser());
        setApiToken(result.getToken());
      }
    });
    task.schedule();
  }

  public void updateUser(io.ayro.User user) {
    User ayroUser = AppUtils.getUser(context, user);
    UpdateUserTask task = new UpdateUserTask(context, ayroUser);
    task.setCallback(new TaskCallback<User>() {
      @Override
      public void onSuccess(User user) {
        setUser(user);
      }
    });
    task.schedule();
  }

  public void updatePushToken() {
    UpdatePushTokenTask task = new UpdatePushTokenTask(context);
    task.schedule();
  }

  public boolean hasPendingTasks() {
    return TaskManager.getInstance(context).hasPendingTasks();
  }

  public void openChat() {
    Intent intent = new Intent(context, ChatActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  public boolean isChatOpened() {
    return chatOpened;
  }

  public void setChatOpened(boolean chatOpened) {
    this.chatOpened = chatOpened;
  }

  private void setAppStatus(AppStatus appStatus) {
    this.appStatus = appStatus;
    Store.setAppStatus(context, appStatus);
  }

  private void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
    Store.setUserStatus(context, userStatus);
  }

  private void setSettings(Settings settings) {
    this.settings = settings;
    Store.setSettings(context, settings);
  }

  private void setApp(App app) {
    this.app = app;
    Store.setApp(context, app);
  }

  private void setIntegration(Integration integration) {
    this.integration = integration;
    Store.setIntegration(context, integration);
  }

  private void setUser(User user) {
    this.user = user;
    Store.setUser(context, user);
  }

  private void setApiToken(String apiToken) {
    this.apiToken = apiToken;
    Store.setApiToken(context, apiToken);
  }
}
