package io.ayro.service.payload;

import com.google.gson.annotations.SerializedName;

import io.ayro.model.User;
import io.ayro.model.App;
import io.ayro.model.Integration;

public class InitResult {

  @SerializedName("app")
  private App app;
  @SerializedName("integration")
  private Integration integration;
  @SerializedName("user")
  private User user;
  @SerializedName("token")
  private String token;

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  public Integration getIntegration() {
    return integration;
  }

  public void setIntegration(Integration integration) {
    this.integration = integration;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
