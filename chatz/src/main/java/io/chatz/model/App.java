package io.chatz.model;

import java.io.Serializable;
import java.util.Date;

public class App implements Serializable {

  private static final long serialVersionUID = -4493755216182901335L;

  private String id;
  private String account;
  private String name;
  private String icon;
  private String token;
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
