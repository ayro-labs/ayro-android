package io.ayro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Configuration implements Serializable {

  private static final long serialVersionUID = 2443981414459703620L;

  @SerializedName("primary_color")
  private String primaryColor;
  @SerializedName("conversation_color")
  private String conversationColor;

  public String getPrimaryColor() {
    return primaryColor;
  }

  public void setPrimaryColor(String primaryColor) {
    this.primaryColor = primaryColor;
  }

  public String getConversationColor() {
    return conversationColor;
  }

  public void setConversationColor(String conversationColor) {
    this.conversationColor = conversationColor;
  }
}
