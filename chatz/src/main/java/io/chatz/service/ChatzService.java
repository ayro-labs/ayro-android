package io.chatz.service;

import java.util.List;

import io.chatz.model.ChatMessage;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.iface.ChatzApi;
import io.chatz.service.payload.InitPayload;
import io.chatz.service.payload.InitResult;
import io.chatz.service.payload.LoginPayload;
import io.chatz.service.payload.LoginResult;
import io.chatz.service.payload.PostMessagePayload;
import io.chatz.util.JsonUtils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatzService implements ChatzApi {

  private static final String API_URL = "http://192.168.15.5:3000";

  private static ChatzService instance;

  private ChatzApi chatzApi;

  private ChatzService() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson())).build();
    chatzApi = retrofit.create(ChatzApi.class);
  }

  public static ChatzService getInstance() {
    if (instance == null) {
      instance = new ChatzService();
    }
    return instance;
  }

  @Override
  public Call<InitResult> init(InitPayload payload) {
    return chatzApi.init(payload);
  }

  @Override
  public Call<LoginResult> login(LoginPayload payload) {
    return chatzApi.login(payload);
  }

  @Override
  public Call<Void> logout(String apiToken) {
    return chatzApi.logout(apiToken);
  }

  @Override
  public Call<User> updateUser(String apiToken, User user) {
    return chatzApi.updateUser(apiToken, user);
  }

  @Override
  public Call<Device> updateDevice(String apiToken, Device device) {
    return chatzApi.updateDevice(apiToken, device);
  }

  @Override
  public Call<ChatMessage> postMessage(String apiToken, PostMessagePayload payload) {
    return chatzApi.postMessage(apiToken, payload);
  }

  @Override
  public Call<List<ChatMessage>> listMessages(String apiToken) {
    return chatzApi.listMessages(apiToken);
  }
}
