package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import io.ayro.exception.TaskException;
import io.ayro.service.AyroService;
import io.ayro.service.payload.LogoutResult;
import io.ayro.store.Store;
import io.ayro.task.Task;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class LogoutTask extends Task<LogoutResult> {

  private static final String TASK_NAME = "user.logout";

  private static final String GENERIC_ERROR_CODE = "logout_error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not sign out";

  private AyroService ayroService;

  public LogoutTask(Context context) {
    super(context, TASK_NAME);
    this.ayroService = AyroService.getInstance();
  }

  @Override
  protected LogoutResult executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Signing out...", TASK_NAME));
      String apiToken = Store.getApiToken(getContext());
      if (apiToken == null) {
        return null;
      }
      Response<LogoutResult> response = ayroService.logout(apiToken).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) User signed out with success!", TASK_NAME));
        return response.body();
      }
      TaskException exception = new TaskException(response, true);
      Log.e(Constants.TAG, String.format("(%s) Could not sign out: %s", TASK_NAME, MessageUtils.get(exception)));
      throw exception;
    } catch (TaskException e) {
      throw e;
    } catch (Exception e) {
      TaskException exception = new TaskException(GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
