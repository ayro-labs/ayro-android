package io.ayro.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.UUID;

import io.ayro.model.Device;
import io.ayro.model.DeviceInfo;
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
    ayroUser.setEmail(user.getEmail());
    ayroUser.setPhotoUrl(user.getPhotoUrl());
    ayroUser.setSignUpDate(user.getSignUpDate());
    ayroUser.setPhotoUrl(user.getPhotoUrl());
    if (user.getUid() == null) {
      ayroUser.setUid(getUserUid(context));
      ayroUser.setIdentified(false);
    } else {
      ayroUser.setIdentified(true);
    }
    return ayroUser;
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
