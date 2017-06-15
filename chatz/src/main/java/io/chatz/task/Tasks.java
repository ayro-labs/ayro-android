package io.chatz.task;

import java.util.HashMap;
import java.util.Map;

public class Tasks {

  private static Map<Class<? extends Task>, Task> tasks = new HashMap<>();

  public static <T> void execute(Task<T> task, Task.TaskCallback<T> callback) {
    if(!tasks.containsKey(task.getClass())) {
      task.execute(callback);
      tasks.put(task.getClass(), task);
    }
  }

  public static void cancel(Class<? extends Task> taskClass) {
    Task task = tasks.remove(taskClass);
    if(task != null) {
      task.cancel();
    }
  }

  public static void cancelAll() {
    for(Task task : tasks.values()) {
      task.cancel();
    }
    tasks.clear();
  }
}