package io.chatz.service.payload;

import io.chatz.model.Device;
import io.chatz.model.User;

public class LoginRequest {

  private User user;
  private Device device;
  private String appToken;

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

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }
}