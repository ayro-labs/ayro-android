package io.ayro;

import android.app.Application;
import android.util.Log;

import io.ayro.core.AyroApp;
import io.ayro.enums.AppStatus;
import io.ayro.enums.UserStatus;
import io.ayro.exception.AyroException;
import io.ayro.util.Constants;

public class Ayro {

  private static AyroApp ayroApp;

  private Ayro() {

  }

  public static AppStatus getAppStatus() {
    if (ayroApp == null) {
      return AppStatus.NOT_INITIALIZED;
    }
    return ayroApp.getAppStatus();
  }

  public static UserStatus getUserStatus() {
    if (ayroApp == null) {
      return UserStatus.LOGGED_OUT;
    }
    return ayroApp.getUserStatus();
  }

  public static void init(Application application, Settings settings) {
    ayroApp = AyroApp.getInstance(application);
    ayroApp.init(settings);
  }

  public static void login(User user, String jwtToken) {
    assertInitCalledFirst();
    ayroApp.login(user, jwtToken);
  }

  public static void logout() {
    assertInitCalledFirst();
    ayroApp.logout();
  }

  public static void updateUser(User user) {
    assertInitCalledFirst();
    ayroApp.updateUser(user);
  }

  public static void openChat() {
    assertInitCalledFirst();
    ayroApp.openChat();
  }

  private static void assertInitCalledFirst() {
    if (ayroApp == null) {
      AyroException exception = new AyroException("Init method should be called first!");
      Log.e(Constants.TAG, exception.getMessage());
      throw exception;
    }
  }
}
