package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import io.ayro.exception.TaskException;
import io.ayro.model.Device;
import io.ayro.service.AyroService;
import io.ayro.service.payload.InitPayload;
import io.ayro.service.payload.InitResult;
import io.ayro.task.Task;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class InitTask extends Task<InitResult> {

  private static final String TASK_NAME = "app.init";

  private static final String GENERIC_ERROR_CODE = "init_error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not initialize the library";

  private InitPayload payload;
  private AyroService service;

  public InitTask(Context context, String appToken, Device device) {
    super(context, TASK_NAME);
    this.payload = new InitPayload(appToken, device);
    this.service = AyroService.getInstance();
  }

  @Override
  protected InitResult executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Initializing Ayro...", TASK_NAME));
      Response<InitResult> response = service.init(payload).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) Ayro was initialized with success!", TASK_NAME));
        return response.body();
      }
      TaskException exception = new TaskException(response, true);
      Log.e(Constants.TAG, String.format("(%s) Could not initialize Ayro: %s", TASK_NAME, MessageUtils.get(exception)));
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
