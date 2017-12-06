package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import io.ayro.service.AyroService;
import io.ayro.store.Store;
import io.ayro.task.Task;
import io.ayro.exception.TaskException;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class LogoutTask extends Task<Void> {

  private static final String TASK_NAME = "logout";

  private static final String GENERIC_ERROR_STATUS = "999";
  private static final String GENERIC_ERROR_CODE = "logout.error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not sign out";

  private static final String PERMISSION_DENIED_ERROR_CODE = "permission.denied";

  private Context context;
  private AyroService ayroService;

  public LogoutTask(Context context) {
    super(context, TASK_NAME);
    this.context = context;
    this.ayroService = AyroService.getInstance();
  }

  @Override
  protected Void executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Signing out...", TASK_NAME));
      String apiToken = Store.getApiToken(context);
      Response<Void> response = ayroService.logout(apiToken).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) User signed out with success!", TASK_NAME));
        return null;
      } else {
        TaskException exception = new TaskException(response, true);
        if (PERMISSION_DENIED_ERROR_CODE.equals(exception.getCode())) {
          return null;
        }
        Log.e(Constants.TAG, String.format("(%s) Could not sign out: %s", TASK_NAME, MessageUtils.get(exception)));
        throw exception;
      }
    } catch (IOException e) {
      TaskException exception = new TaskException(GENERIC_ERROR_STATUS, GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
