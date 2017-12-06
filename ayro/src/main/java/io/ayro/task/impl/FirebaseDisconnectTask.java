package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import io.ayro.task.Task;
import io.ayro.exception.TaskException;
import io.ayro.util.Constants;

public class FirebaseDisconnectTask extends Task<Void> {

  private static final String TASK_NAME = "firebase.disconnect";

  public FirebaseDisconnectTask(Context context) {
    super(context, TASK_NAME);
  }

  @Override
  protected Void executeJob() throws TaskException {
    try {
      Log.d(Constants.TAG, String.format("(%s) Disconnecting from Firebase...", TASK_NAME));
      FirebaseInstanceId.getInstance().deleteInstanceId();
    } catch (IOException e) {
      // Nothing to do...
    }
    Log.d(Constants.TAG, String.format("(%s) Firebase was disconnected with success", TASK_NAME));
    return null;
  }
}
