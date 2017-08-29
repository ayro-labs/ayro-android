package io.chatz.task;

import android.content.Context;

import io.chatz.exception.TaskException;

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
    Tasks.getInstance(context).execute(this);
  }

  public void schedule() {
    Tasks.getInstance(context).schedule(this);
  }
}