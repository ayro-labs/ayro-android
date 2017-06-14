package io.chatz;

public class Settings {

  private String projectToken;
  private boolean messagingServiceEnabled = true;

  public Settings(String projectToken) {
    this.projectToken = projectToken;
  }

  public String getProjectToken() {
    return projectToken;
  }

  public void setProjectToken(String projectToken) {
    this.projectToken = projectToken;
  }

  public boolean isMessagingServiceEnabled() {
    return messagingServiceEnabled;
  }

  public void setMessagingServiceEnabled(boolean messagingServiceEnabled) {
    this.messagingServiceEnabled = messagingServiceEnabled;
  }
}