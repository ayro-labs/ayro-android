package io.ayro.task;

import android.content.Context;

import io.ayro.exception.TaskException;

public abstract class Task<T> {

  private Context context;
  private String name;
  private TaskCallback<T> callback;

  public Task(Context context, String name) {
    this.context = context;
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
    TaskManager.getInstance(context).execute(this);
  }

  public void schedule() {
    TaskManager.getInstance(context).schedule(this);
  }
}
