package io.ayro.service.payload;

public class TrackViewChatPayload {

  private String channel;

  public TrackViewChatPayload(String channel) {
    this.channel = channel;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
