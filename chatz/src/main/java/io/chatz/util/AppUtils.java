package io.chatz.util;

import android.content.Context;

public class AppUtils {

  public static String getDeviceId(Context context) {
    return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
  }
}
