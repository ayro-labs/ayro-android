package io.ayro.service.iface;

import java.util.List;

import io.ayro.model.ChatMessage;
import io.ayro.model.Device;
import io.ayro.model.User;
import io.ayro.service.payload.InitPayload;
import io.ayro.service.payload.InitResult;
import io.ayro.service.payload.LoginPayload;
import io.ayro.service.payload.LoginResult;
import io.ayro.service.payload.PostMessagePayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AyroApi {

  @POST("/apps/integrations/android/init")
  Call<InitResult> init(@Body InitPayload payload);

  @POST("/auth/users")
  Call<LoginResult> login(@Body LoginPayload payload);

  @DELETE("/auth/users")
  Call<Void> logout(@Header("X-Token") String apiToken);

  @PUT("/users")
  Call<User> updateUser(@Header("X-Token") String apiToken, @Body User user);

  @PUT("/users/devices")
  Call<Device> updateDevice(@Header("X-Token") String apiToken, @Body Device device);

  @POST("/chat/android")
  Call<ChatMessage> postMessage(@Header("X-Token") String apiToken, @Body PostMessagePayload payload);

  @GET("/chat")
  Call<List<ChatMessage>> listMessages(@Header("X-Token") String apiToken);

}
