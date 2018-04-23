package io.ayro.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER = "Bearer";

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder requestBuilder = request.newBuilder();
    String authorizationValue = request.header(AUTHORIZATION_HEADER);
    if (authorizationValue != null && !authorizationValue.startsWith(BEARER)) {
      requestBuilder.header(AUTHORIZATION_HEADER, BEARER + " " + authorizationValue);
    }
    return chain.proceed(requestBuilder.build());
  }
}
