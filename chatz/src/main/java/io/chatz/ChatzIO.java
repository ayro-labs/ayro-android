package io.chatz;

import android.app.Application;

import io.chatz.model.User;

public class ChatzIO {

  private static Chatz chatz;

  private ChatzIO() {

  }

  public static void init(Application application, Settings settings) {
    chatz = Chatz.getInstance(application);
    chatz.init(settings);
  }

  public static void login(User user) {
    chatz.login(user);
  }

  public static void logout() {
    chatz.logout();
  }

  public static void updateUser(User user) {
    chatz.updateUser(user);
  }

  public static void openChat() {
    chatz.openChat();
  }
}