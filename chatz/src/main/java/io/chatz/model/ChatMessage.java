package io.chatz.model;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

  private static final long serialVersionUID = 5024908185070932244L;

  private Author author;
  private String text;
  private Status status;
  private Direction direction;
  private Date date;

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
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
    sending, sent, error
  }

  public enum Direction {
    incoming, outgoing;
  }
}
