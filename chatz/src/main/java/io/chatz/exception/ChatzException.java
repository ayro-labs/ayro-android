package io.chatz.exception;

public class ChatzException extends RuntimeException {

  public ChatzException(String message) {
    super(message);
  }

  public ChatzException(String message, Throwable cause) {
    super(message, cause);
  }
}