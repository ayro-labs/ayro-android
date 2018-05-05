package io.ayro.service.iface;

import java.util.List;

import io.ayro.model.ChatMessage;
import io.ayro.model.Device;
import io.ayro.model.User;
import io.ayro.service.payload.InitPayload;
import io.ayro.service.payload.InitResult;
import io.ayro.service.payload.LoginPayload;
import io.ayro.service.payload.LoginResult;
import io.ayro.service.payload.LogoutResult;
import io.ayro.service.payload.PostMessagePayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AyroApi {

  @POST("/apps/integrations/android/init")
  Call<InitResult> init(@Body InitPayload payload);

  @POST("/users/login")
  Call<LoginResult> login(@Header("Authorization") String apiToken, @Body LoginPayload payload);

  @POST("/users/logout")
  Call<LogoutResult> logout(@Header("Authorization") String apiToken);

  @PUT("/users")
  Call<User> updateUser(@Header("Authorization") String apiToken, @Body User user);

  @PUT("/users/devices")
  Call<Device> updateDevice(@Header("Authorization") String apiToken, @Body Device device);

  @POST("/chat")
  Call<ChatMessage> postMessage(@Header("Authorization") String apiToken, @Body PostMessagePayload payload);

  @GET("/chat")
  Call<List<ChatMessage>> listMessages(@Header("Authorization") String apiToken);

  @POST("/events/view_chat")
  Call<Void> trackViewChat(@Header("Authorization") String apiToken);

}
