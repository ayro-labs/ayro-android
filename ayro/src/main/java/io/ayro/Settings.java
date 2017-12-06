package io.ayro;

public class Settings {

  private String appToken;
  private boolean messagingServiceEnabled;

  public Settings(String appToken) {
    this.appToken = appToken;
  }

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }

  public boolean isMessagingServiceEnabled() {
    return messagingServiceEnabled;
  }

  public void enableMessagingService() {
    this.messagingServiceEnabled = true;
  }

  public void disableMessagingService() {
    this.messagingServiceEnabled = false;
  }
}
