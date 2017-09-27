package io.chatz.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ImageUtils {

  private ImageUtils() {

  }

  public static Bitmap getCircularBitmap(Bitmap bitmap) {
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

    Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    RectF rectF = new RectF(rect);

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(Color.WHITE);

    Canvas canvas = new Canvas(output);
    canvas.drawARGB(0, 0, 0, 0);
    canvas.drawOval(rectF, paint);

    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    return output;
  }

  public static void setPicture(Context context, String photoUrl, final ImageView imageView) {
    new LoadPictureTask(context, photoUrl, new Callback<Bitmap>() {
      @Override
      public void onSuccess(Bitmap bitmap) {
        if (bitmap != null) {
          imageView.setImageBitmap(bitmap);
        }
      }

      @Override
      public void onFail(Exception exception) {

      }
    }).execute((Void) null);
  }

  public static void loadPicture(Context context, String photoUrl, Callback<Bitmap> callback) {
    new LoadPictureTask(context, photoUrl, callback).execute((Void) null);
  }

  private static class LoadPictureTask extends AsyncTask<Void, Void, Bitmap> {

    private Context context;
    private String photoUrl;
    private Callback<Bitmap> callback;

    private LoadPictureTask(Context context, String photoUrl, Callback<Bitmap> callback) {
      this.context = context;
      this.photoUrl = photoUrl;
      this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
      if (photoUrl != null) {
        try {
          return Picasso.with(context).load(photoUrl).get();
        } catch (IOException e) {
          if (callback != null) {
            callback.onFail(e);
          }
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      if (callback != null) {
        callback.onSuccess(bitmap);
      }
    }
  }
}
