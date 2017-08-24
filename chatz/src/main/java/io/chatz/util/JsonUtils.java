package io.chatz.util;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class JsonUtils {

  private static Gson gson = createGson();

  private static Gson createGson() {
    return new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  }

  public static Gson getGson() {
    return gson;
  }

  public static String toJson(Object object) {
    return gson.toJson(object);
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  public static <T> T fromJson(String json, Type type) {
    return gson.fromJson(json, type);
  }

  private static class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final String TAG = DateSerializer.class.getSimpleName();

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(createDateFormat().format(src));
    }

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
      Date deserialized = null;
      try {
        deserialized = createDateFormat().parse(json.getAsString());
      } catch (ParseException e) {
        Log.w(TAG, "Error deserializing date", e);
      }
      return deserialized;
    }

    private DateFormat createDateFormat() {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
      return dateFormat;
    }
  }
}
