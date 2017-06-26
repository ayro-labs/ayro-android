package io.chatz;

public enum Status {

  NOT_INITIALIZED(0), INITIALIZED(1), LOGGED_IN(2), LOGGED_OUT(3);

  private int order;

  Status(int order) {
    this.order = order;
  }

  public int getOrder() {
    return order;
  }
}