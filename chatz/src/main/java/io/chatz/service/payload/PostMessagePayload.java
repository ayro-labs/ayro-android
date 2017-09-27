package io.chatz.service.payload;

public class PostMessagePayload {

  private String text;

  public PostMessagePayload(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
