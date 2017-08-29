package io.chatz.task.impl;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import io.chatz.service.ChatzService;
import io.chatz.service.payload.InitPayload;
import io.chatz.service.payload.InitResult;
import io.chatz.task.Task;
import io.chatz.exception.TaskException;
import io.chatz.util.Constants;
import io.chatz.util.MessageUtils;
import retrofit2.Response;

public class InitTask extends Task<InitResult> {

  private static final String TASK_NAME = "init";

  private static final String GENERIC_ERROR_STATUS = "999";
  private static final String GENERIC_ERROR_CODE = "init.error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not initialize the library";

  private InitPayload payload;
  private ChatzService chatzService;

  public InitTask(Context context, String appToken) {
    super(context, TASK_NAME);
    this.payload = new InitPayload(appToken);
    this.chatzService = ChatzService.getInstance();
  }

  @Override
  protected InitResult executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Initializing Chatz...", TASK_NAME));
      Response<InitResult> response = chatzService.init(payload).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) Chatz was initialized with success!", TASK_NAME));
        return response.body();
      } else {
        TaskException exception = new TaskException(response, true);
        Log.e(Constants.TAG, String.format("(%s) Could not initialize Chatz: %s", TASK_NAME, MessageUtils.get(exception)));
        throw exception;
      }
    } catch (IOException e) {
      TaskException exception = new TaskException(GENERIC_ERROR_STATUS, GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}