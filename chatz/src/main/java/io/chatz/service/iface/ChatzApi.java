package io.chatz.service.iface;

import java.util.List;

import io.chatz.model.ChatMessage;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.payload.InitPayload;
import io.chatz.service.payload.InitResult;
import io.chatz.service.payload.LoginPayload;
import io.chatz.service.payload.LoginResult;
import io.chatz.service.payload.PostMessagePayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ChatzApi {

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
