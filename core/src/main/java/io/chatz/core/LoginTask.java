package io.chatz.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import io.chatz.core.model.AuthenticationPayload;
import io.chatz.core.model.Device;
import io.chatz.core.model.User;
import io.chatz.core.service.Services;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTask {

  private static final String THREAD_NAME = "ChatzIO.login";
  private static final long SLEEP = 10000L;

  private HandlerThread thread;
  private AuthenticationPayload payload;
  private LoginCallback callback;
  private int executions;

  public LoginTask login(String projectToken, User user, Device device) {
    thread = new HandlerThread(THREAD_NAME);
    thread.start();
    payload = new AuthenticationPayload();
    payload.setProjectToken(projectToken);
    payload.setUser(user);
    payload.setDevice(device);
    return this;
  }

  public void exec(final LoginCallback callback) {
    this.callback = callback;
    new Handler(thread.getLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        sendRequest();
      }
    }, SLEEP * executions);
  }

  private void sendRequest() {
    executions++;
    Services.getInstance().getApiService().authenticateCustomer(payload).enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if(callback != null) {
          callback.onLoggedIn(response.body());
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable throwable) {
        Log.e(Constants.TAG, "Could not authenticate user, retrying in few seconds...");
        exec(callback);
      }
    });
  }

  public interface LoginCallback {

    void onLoggedIn(String token);

  }
}