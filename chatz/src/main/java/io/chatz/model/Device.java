package io.chatz.model;

public class Device {

  private String uid;
  private String platform;
  private String appId;
  private String appVersion;
  private String pushToken;
  private DeviceInfo info;

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getPushToken() {
    return pushToken;
  }

  public void setPushToken(String pushToken) {
    this.pushToken = pushToken;
  }

  public DeviceInfo getInfo() {
    return info;
  }

  public void setInfo(DeviceInfo info) {
    this.info = info;
  }
}