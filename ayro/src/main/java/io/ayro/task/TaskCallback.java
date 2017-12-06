package io.ayro.task;

import io.ayro.exception.TaskException;

public class TaskCallback<T> {

  public void onSuccess(T result) {

  }

  public void onFail(TaskException e) {

  }
}
