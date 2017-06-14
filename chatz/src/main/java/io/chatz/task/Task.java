package io.chatz.task;

import android.os.Handler;
import android.os.HandlerThread;

public abstract class Task<T> {

  private static final long SLEEP = 10000L;

  private HandlerThread thread;
  private TaskCallback<T> callback;
  private int executions;

  public Task(String threadName) {
    thread = new HandlerThread(threadName);
    thread.start();
  }

  public abstract void executeJob();

  protected void success(T result) {
    if(callback != null) {
      callback.onSuccess(result);
    }
  }

  protected void fail() {
    exec(callback);
  }

  public void exec(TaskCallback<T> callback) {
    this.callback = callback;
    new Handler(thread.getLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        executions++;
        executeJob();
      }
    }, SLEEP * executions);
  }

  public interface TaskCallback<T> {

    void onSuccess(T result);

  }
}