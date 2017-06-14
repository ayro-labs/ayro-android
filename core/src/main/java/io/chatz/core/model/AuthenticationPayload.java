package io.chatz.core.model;

public class AuthenticationPayload {

  private User user;
  private Device device;
  private String projectToken;

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

  public String getProjectToken() {
    return projectToken;
  }

  public void setProjectToken(String projectToken) {
    this.projectToken = projectToken;
  }
}