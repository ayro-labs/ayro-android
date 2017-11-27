package io.ayro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Integration implements Serializable {

  public static final String PRIMARY_COLOR_CONFIGURATION = "primary_color";
  public static final String CONVERSATION_COLOR_CONFIGURATION = "conversation_color";

  @SerializedName("id")
  private String id;
  @SerializedName("type")
  private String type;
  @SerializedName("channel")
  private String channel;
  @SerializedName("configuration")
  private Map<String, String> configuration;
  @SerializedName("registration_date")
  private Date registrationDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public Map<String, String> getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Map<String, String> configuration) {
    this.configuration = configuration;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}
