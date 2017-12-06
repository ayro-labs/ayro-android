package io.ayro.service.payload;

import com.google.gson.annotations.SerializedName;

import io.ayro.model.App;
import io.ayro.model.Integration;

public class InitResult {

  @SerializedName("app")
  private App app;
  @SerializedName("integration")
  private Integration integration;

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
}
