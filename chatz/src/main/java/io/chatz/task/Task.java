package io.chatz.task;

import io.chatz.exception.TaskException;

public abstract class Task<T> {

  private String name;
  private TaskCallback<T> callback;

  public Task(String name) {
    this.name = name;
  }

  protected abstract T executeJob() throws TaskException;

  public String getName() {
    return name;
  }

  public TaskCallback<T> getCallback() {
    return callback;
  }

  public void setCallback(TaskCallback<T> callback) {
    this.callback = callback;
  }

  public void execute() {
    Tasks.getInstance().execute(this);
  }

  public void schedule() {
    Tasks.getInstance().schedule(this);
  }
}