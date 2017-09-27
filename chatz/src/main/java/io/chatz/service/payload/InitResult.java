package io.chatz.service.payload;

import io.chatz.model.App;
import io.chatz.model.Integration;

public class InitResult {

  private App app;
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
