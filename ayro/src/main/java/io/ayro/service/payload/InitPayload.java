package io.ayro.service.payload;

import com.google.gson.annotations.SerializedName;

import io.ayro.model.Device;

public class InitPayload {

  @SerializedName("app_token")
  private String appToken;
  @SerializedName("device")
  private Device device;

  public InitPayload(String appToken, Device device) {
    this.appToken = appToken;
    this.device = device;
  }

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }
}
