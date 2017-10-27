package io.chatz.model;

import java.io.Serializable;

public class Device implements Serializable {

  private static final long serialVersionUID = -8830545575166750694L;

  private String id;
  private String uid;
  private String platform;
  private String pushToken;
  private DeviceInfo info;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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
