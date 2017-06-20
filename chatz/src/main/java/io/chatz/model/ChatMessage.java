package io.chatz.model;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

  private static final long serialVersionUID = 5024908185070932244L;

  private String userName;
  private String userPhoto;
  private String text;
  private Status status;
  private Direction direction;
  private Date date;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPhoto() {
    return userPhoto;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public enum Status {
    SENDING, SENT, ERROR_SENDING
  }

  public enum Direction {
    INCOMING, OUTGOING;
  }
}