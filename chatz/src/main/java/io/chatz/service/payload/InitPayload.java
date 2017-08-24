package io.chatz.service.payload;

public class InitPayload {

  private String appToken;

  public InitPayload(String appToken) {
    this.appToken = appToken;
  }

  public String getAppToken() {
    return appToken;
  }

  public void setAppToken(String appToken) {
    this.appToken = appToken;
  }
}