package io.ayro.service;

import java.util.List;

import io.ayro.BuildConfig;
import io.ayro.model.ChatMessage;
import io.ayro.model.Device;
import io.ayro.model.User;
import io.ayro.service.iface.AyroApi;
import io.ayro.service.payload.InitPayload;
import io.ayro.service.payload.InitResult;
import io.ayro.service.payload.LoginPayload;
import io.ayro.service.payload.LoginResult;
import io.ayro.service.payload.PostMessagePayload;
import io.ayro.util.JsonUtils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AyroService implements AyroApi {

  private static AyroService instance;

  private AyroApi ayroApi;

  private AyroService() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.API_URL).addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson())).build();
    ayroApi = retrofit.create(AyroApi.class);
  }

  public static AyroService getInstance() {
    if (instance == null) {
      instance = new AyroService();
    }
    return instance;
  }

  @Override
  public Call<InitResult> init(InitPayload payload) {
    return ayroApi.init(payload);
  }

  @Override
  public Call<LoginResult> login(LoginPayload payload) {
    return ayroApi.login(payload);
  }

  @Override
  public Call<Void> logout(String apiToken) {
    return ayroApi.logout(apiToken);
  }

  @Override
  public Call<User> updateUser(String apiToken, User user) {
    return ayroApi.updateUser(apiToken, user);
  }

  @Override
  public Call<Device> updateDevice(String apiToken, Device device) {
    return ayroApi.updateDevice(apiToken, device);
  }

  @Override
  public Call<ChatMessage> postMessage(String apiToken, PostMessagePayload payload) {
    return ayroApi.postMessage(apiToken, payload);
  }

  @Override
  public Call<List<ChatMessage>> listMessages(String apiToken) {
    return ayroApi.listMessages(apiToken);
  }
}
