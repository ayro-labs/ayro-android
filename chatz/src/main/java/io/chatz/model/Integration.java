package io.chatz.model;

import java.util.Date;
import java.util.Map;

public class Integration {

  public static final String PRIMARY_COLOR_CONFIGURATION = "primary_color";
  public static final String CONVERSATION_COLOR_CONFIGURATION = "conversation_color";

  public String type;
  public String channel;
  public Map<String, String> configuration;
  public Date registration_date;

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
