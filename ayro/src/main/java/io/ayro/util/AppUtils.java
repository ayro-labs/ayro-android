package io.ayro.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.UUID;

import io.ayro.R;
import io.ayro.core.AyroApp;
import io.ayro.model.Device;
import io.ayro.model.DeviceInfo;
import io.ayro.model.Integration;
import io.ayro.model.User;
import io.ayro.store.Store;

public class AppUtils {

  private AppUtils() {

  }

  public static User getUser(Context context, io.ayro.User user) {
    User ayroUser = new User();
    ayroUser.setUid(user.getUid());
    ayroUser.setFirstName(user.getFirstName());
    ayroUser.setLastName(user.getLastName());
    ayroUser.setPhotoUrl(user.getPhotoUrl());
    ayroUser.setEmail(user.getEmail());
    ayroUser.setSignUpDate(user.getSignUpDate());
    ayroUser.setProperties(user.getProperties());
    return ayroUser;
  }


  public static Device getDevice(Context context) {
    DeviceInfo info = new DeviceInfo();
    info.setOperatingSystem(Constants.OS_NAME + " " + Build.VERSION.RELEASE);
    info.setManufacturer(Build.MANUFACTURER);
    info.setModel(Build.MODEL);
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null) {
      info.setCarrier(telephonyManager.getNetworkOperatorName());
    }
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      info.setAppId(packageInfo.packageName);
      info.setAppVersion(packageInfo.versionName);
    } catch (PackageManager.NameNotFoundException e) {
      // Nothing to do...
    }
    Device device = new Device();
    device.setUid(getDeviceUid(context));
    device.setPlatform(Constants.PLATFORM);
    device.setInfo(info);
    return device;
  }

  public static int getPrimaryColor(Context context) {
    Integration integration = AyroApp.getInstance(context).getIntegration();
    int primaryColor;
    if (integration != null && integration.getConfiguration() != null) {
      String colorHex = integration.getConfiguration().getPrimaryColor();
      primaryColor = Color.parseColor(colorHex);
    } else {
      primaryColor = ContextCompat.getColor(context, R.color.ayro_primary);
    }
    return primaryColor;
  }

  public static int getConversationColor(Context context) {
    Integration integration = AyroApp.getInstance(context).getIntegration();
    int conversationColor;
    if (integration != null && integration.getConfiguration() != null) {
      String colorHex = integration.getConfiguration().getConversationColor();
      conversationColor = Color.parseColor(colorHex);
    } else {
      conversationColor = ContextCompat.getColor(context, R.color.ayro_conversation);
    }
    return conversationColor;
  }

  private static String getDeviceUid(Context context) {
    String uid = Store.getDeviceUid(context);
    if (uid == null) {
      uid = generateUid();
      Store.setDeviceUid(context, uid);
    }
    return uid;
  }

  private static String generateUid() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
