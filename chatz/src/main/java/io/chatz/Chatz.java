package io.chatz;

import android.app.Application;
import android.util.Log;

import io.chatz.core.ChatzApp;
import io.chatz.exception.ChatzException;
import io.chatz.util.Constants;

public class Chatz {

  private static ChatzApp chatzApp;

  private Chatz() {

  }

  public static void init(Application application, Settings settings) {
    chatzApp = ChatzApp.getInstance(application);
    chatzApp.init(settings);
  }

  public static void login(User user) {
    assertInitCalledFirst();
    chatzApp.login(user, null);
  }

  public static void logout() {
    assertInitCalledFirst();
    chatzApp.logout();
  }

  public static void updateUser(User user) {
    assertInitCalledFirst();
    chatzApp.updateUser(user);
  }

  public static void openChat() {
    assertInitCalledFirst();
    chatzApp.openChat();
  }

  private static void assertInitCalledFirst() {
    if (chatzApp == null) {
      ChatzException exception = new ChatzException("Init method should be called first!");
      Log.e(Constants.TAG, exception.getMessage());
      throw exception;
    }
  }
}
