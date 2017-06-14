package io.chatz.core;

public class Settings {

  private String projectToken;

  public Settings(String projectToken) {
    this.projectToken = projectToken;
  }

  public String getProjectToken() {
    return projectToken;
  }
}