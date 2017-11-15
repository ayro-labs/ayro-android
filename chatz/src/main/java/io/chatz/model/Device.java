package io.chatz.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Device implements Serializable {

  private static final long serialVersionUID = -8830545575166750694L;

  @SerializedName("id")
  private String id;
  @SerializedName("uid")
  private String uid;
  @SerializedName("platform")
  private String platform;
  @SerializedName("push_token")
  private String pushToken;
  @SerializedName("info")
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
