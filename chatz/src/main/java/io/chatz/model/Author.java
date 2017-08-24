package io.chatz.model;

import java.io.Serializable;

public class Author implements Serializable {

  private static final long serialVersionUID = 8523803625309951192L;

  private String id;
  private String name;
  private String photo;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
}