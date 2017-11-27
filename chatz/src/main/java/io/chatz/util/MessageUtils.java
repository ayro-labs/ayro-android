package io.ayro.util;

import java.util.HashMap;
import java.util.Map;

import io.ayro.exception.AyroException;

public class MessageUtils {

  private static final String APP_DOES_NOT_EXIST = "app.doesNotExist";

  private static final Map<String, String> MESSAGES = new HashMap<>();

  static {
    MESSAGES.put(APP_DOES_NOT_EXIST, "App does not exist, please make sure you initialized Ayro with the correct app token.");
  }

  private MessageUtils() {

  }

  public static String get(AyroException e) {
    return MESSAGES.containsKey(e.getCode()) ? MESSAGES.get(e.getCode()) : e.getMessage();
  }
}
