package io.chatz;

public class Settings {

  private String appToken;
  private boolean messagingServiceEnabled = true;

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

  public void setMessagingServiceEnabled(boolean messagingServiceEnabled) {
    this.messagingServiceEnabled = messagingServiceEnabled;
  }
}