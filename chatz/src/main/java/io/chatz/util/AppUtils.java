package io.chatz.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.UUID;

import io.chatz.model.Device;
import io.chatz.model.DeviceInfo;
import io.chatz.model.User;
import io.chatz.store.Store;

public class AppUtils {

  private AppUtils() {

  }

  public static User getUser(Context context, io.chatz.User user) {
    User chatzUser = new User();
    chatzUser.setUid(user.getUid());
    chatzUser.setFirstName(user.getFirstName());
    chatzUser.setLastName(user.getLastName());
    chatzUser.setEmail(user.getEmail());
    chatzUser.setPhotoUrl(user.getPhotoUrl());
    chatzUser.setSignUpDate(user.getSignUpDate());
    chatzUser.setPhotoUrl(user.getPhotoUrl());
    if (user.getUid() == null) {
      chatzUser.setUid(getUserUid(context));
      chatzUser.setIdentified(false);
    } else {
      chatzUser.setIdentified(true);
    }
    return chatzUser;
  }

  public static Device getDevice(Context context) {
    DeviceInfo info = new DeviceInfo();
    info.setOperatingSystem(Constants.OS_NAME + " " + Build.VERSION.RELEASE);
    info.setManufacturer(Build.MANUFACTURER);
    info.setModel(Build.MODEL);
    info.setCarrier(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
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

  private static String getUserUid(Context context) {
    String uid = Store.getUserUid(context);
    if (uid == null) {
      uid = generateUid();
      Store.setUserUid(context, uid);
    }
    return uid;
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
