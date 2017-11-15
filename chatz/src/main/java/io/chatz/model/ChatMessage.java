package io.chatz.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

  private static final long serialVersionUID = 5024908185070932244L;

  @SerializedName("id")
  private String id;
  @SerializedName("agent")
  private Agent agent;
  @SerializedName("text")
  private String text;
  @SerializedName("status")
  private Status status;
  @SerializedName("direction")
  private Direction direction;
  @SerializedName("date")
  private Date date;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Agent getAgent() {
    return agent;
  }

  public void setAgent(Agent agent) {
    this.agent = agent;
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
