package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import io.ayro.exception.TaskException;
import io.ayro.task.Task;
import io.ayro.util.Constants;

public class FirebaseDisconnectTask extends Task<Void> {

  private static final String TASK_NAME = "firebase.disconnect";

  public FirebaseDisconnectTask(Context context) {
    super(context, TASK_NAME);
  }

  @Override
  protected Void executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Disconnecting from Firebase...", TASK_NAME));
      FirebaseInstanceId.getInstance().deleteInstanceId();
      Log.i(Constants.TAG, String.format("(%s) Firebase disconnected with success!", TASK_NAME));
    } catch (Exception e) {
      Log.e(Constants.TAG, String.format("(%s) Could not disconnect from Firebase: %s", TASK_NAME, e.getMessage()));
    }
    return null;
  }
}
