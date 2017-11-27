package io.ayro.service.payload;

import com.google.gson.annotations.SerializedName;

public class PostMessagePayload {

  @SerializedName("text")
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
