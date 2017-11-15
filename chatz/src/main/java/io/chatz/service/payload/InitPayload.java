package io.chatz.service.payload;

import com.google.gson.annotations.SerializedName;

public class InitPayload {

  @SerializedName("app_token")
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
