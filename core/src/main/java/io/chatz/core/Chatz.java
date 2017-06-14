package io.chatz.core;

import android.app.Application;

import io.chatz.core.model.User;

public class Chatz {

  private static ChatzIO chatzIO;

  private Chatz() {

  }

  public static void init(Application application, Settings settings) {
    chatzIO = ChatzIO.getInstance(application);
    chatzIO.init(settings);
  }

  public static void login(User user) {
    chatzIO.login(user);
  }

  public static void logout() {
    chatzIO.logout();
  }

  public static User getUser() {
    return chatzIO.getUser();
  }

  public static void updateUser() {
    chatzIO.updateUser();
  }

  public static void openChat() {
    chatzIO.openChat();
  }
}