package io.chatz.util;

import java.util.HashMap;
import java.util.Map;

import io.chatz.exception.ChatzException;

public class MessageUtils {

  private static final String APP_DOES_NOT_EXIST_CODE = "app.doesNotExist";

  private static final Map<String, String> MESSAGES = new HashMap<>();

  static {
    MESSAGES.put(APP_DOES_NOT_EXIST_CODE, "App does not exist, please make sure you initialize Chatz with the correct app token.");
  }

  public static String get(ChatzException e) {
    return MESSAGES.containsKey(e.getCode()) ? MESSAGES.get(e.getCode()) : e.getMessage();
  }
}
