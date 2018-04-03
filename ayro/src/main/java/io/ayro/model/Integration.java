package io.ayro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Integration implements Serializable {

  private static final long serialVersionUID = 7056893270553894515L;

  @SerializedName("id")
  private String id;
  @SerializedName("type")
  private String type;
  @SerializedName("channel")
  private String channel;
  @SerializedName("configuration")
  private Configuration configuration;
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

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}
