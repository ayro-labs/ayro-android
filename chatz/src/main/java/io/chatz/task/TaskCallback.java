package io.chatz.task;

import io.chatz.exception.TaskException;

public class TaskCallback<T> {

  public void onSuccess(T result) {

  }

  public void onFail(TaskException e) {

  }
}
