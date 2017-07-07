package io.chatz.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import io.chatz.model.Device;
import io.chatz.model.DeviceInfo;

public class AppUtils {

  public static String getDeviceId(Context context) {
    return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
  }

  public static Device getDevice(Context context) {
    DeviceInfo info = new DeviceInfo();
    info.setOsName(Constants.OS_NAME);
    info.setOsVersion(Build.VERSION.RELEASE);
    info.setManufacturer(Build.MANUFACTURER);
    info.setModel(Build.MODEL);
    info.setCarrier(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      info.setAppId(packageInfo.packageName);
      info.setAppVersion(packageInfo.versionName);
    } catch(PackageManager.NameNotFoundException e) {
      // Nothing to do...
    }
    Device device = new Device();
    device.setUid(getDeviceId(context));
    device.setPlatform(Constants.PLATFORM);
    device.setInfo(info);
    return device;
  }
}
