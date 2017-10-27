package io.chatz.model;

import java.util.Date;
import java.util.Map;

public class Integration {

  public static final String PRIMARY_COLOR_CONFIGURATION = "primary_color";
  public static final String CONVERSATION_COLOR_CONFIGURATION = "conversation_color";

  private String id;
  private String type;
  private String channel;
  private Map<String, String> configuration;
  private Date registration_date;

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

  public Date getRegistration_date() {
    return registration_date;
  }

  public void setRegistration_date(Date registration_date) {
    this.registration_date = registration_date;
  }
}
