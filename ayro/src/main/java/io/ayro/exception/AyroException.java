package io.ayro.exception;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

import io.ayro.util.JsonUtils;
import retrofit2.Response;

public class AyroException extends RuntimeException {

  private static final String UNKNOWN_ERROR_CODE = "unknown_error";
  private static final String UNKNOWN_ERROR_MESSAGE = "Unknown error";

  private static final String CODE_KEY = "code";
  private static final String MESSAGE_KEY = "message";

  private String code;
  private String message;

  public AyroException(String code, String message) {
    this(code, message, null);
  }

  public AyroException(String code, String message, Throwable cause) {
    super(cause);
    this.code = code;
    this.message = message;
  }

  public AyroException(Response response) {
    if (response.errorBody() != null) {
      try {
        Map<String, String> error = JsonUtils.fromJson(response.errorBody().string(), new TypeToken<Map<String, String>>() {
        }.getType());
        this.code = error.get(CODE_KEY);
        this.message = error.get(MESSAGE_KEY);
      } catch (IOException e) {
        setAsUnknownError();
      }
    } else {
      setAsUnknownError();
    }
  }

  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  private void setAsUnknownError() {
    this.code = UNKNOWN_ERROR_CODE;
    this.message = UNKNOWN_ERROR_MESSAGE;
  }
}
