package io.chatz.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class App implements Serializable {

  private static final long serialVersionUID = -4493755216182901335L;

  @SerializedName("id")
  private String id;
  @SerializedName("account")
  private String account;
  @SerializedName("name")
  private String name;
  @SerializedName("icon")
  private String icon;
  @SerializedName("token")
  private String token;
  @SerializedName("registration_date")
  private Date registrationDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
}
