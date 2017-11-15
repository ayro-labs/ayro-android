package io.chatz.service.payload;

import com.google.gson.annotations.SerializedName;

import io.chatz.model.Device;
import io.chatz.model.User;

public class LoginPayload {

  @SerializedName("app_token")
  private String appToken;
  @SerializedName("user")
  private User user;
  @SerializedName("device")
  private Device device;

  public LoginPayload(String appToken, User user, Device device) {
    this.appToken = appToken;
    this.user = user;
    this.device = device;
  }

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }
}
