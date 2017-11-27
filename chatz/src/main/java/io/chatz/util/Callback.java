package io.ayro.util;

public interface Callback<T> {

  void onSuccess(T result);

  void onFail(Exception e);

}
