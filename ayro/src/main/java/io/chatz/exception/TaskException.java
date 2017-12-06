package io.ayro.exception;

import retrofit2.Response;

public class TaskException extends AyroException {

  private boolean cancelTask;

  public TaskException(String status, String code, String message, boolean cancelTask) {
    super(status, code, message);
    this.cancelTask = cancelTask;
  }

  public TaskException(String status, String code, String message, Throwable cause, boolean cancelTask) {
    super(status, code, message, cause);
    this.cancelTask = cancelTask;
  }

  public TaskException(Response response, boolean cancelTask) {
    super(response);
    this.cancelTask = cancelTask;
  }

  public boolean shouldCancelTask() {
    return cancelTask;
  }
}
