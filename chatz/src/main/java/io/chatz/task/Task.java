package io.chatz.task;

import android.os.Handler;
import android.os.HandlerThread;

import io.chatz.util.Callback;

public abstract class Task<T> {

  private static final long SLEEP = 10000L;

  private Handler handler;
  private Callback<T> callback;
  private int executions;
  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      executions++;
      executeJob();
    }
  };

  public Task(String name) {
    HandlerThread thread = new HandlerThread(name);
    thread.start();
    handler = new Handler(thread.getLooper());
  }

  protected abstract void executeJob();

  protected void success(T result) {
    Tasks.cancel(getClass());
    if(callback != null) {
      callback.onSuccess(result);
    }
  }

  protected void fail() {
    execute(callback);
  }

  void execute(Callback<T> callback) {
    this.callback = callback;
    handler.postDelayed(runnable, SLEEP * executions);
  }

  void cancel() {
    handler.removeCallbacks(runnable);
  }
}