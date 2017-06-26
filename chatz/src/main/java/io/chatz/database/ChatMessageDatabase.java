package io.chatz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.chatz.model.Author;
import io.chatz.model.ChatMessage;

public class ChatMessageDatabase extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "chatz.db";
  private static final Integer DATABASE_VERSION = 1;

  private static final String TABLE_NAME = "chat_message";
  private static final String COLUMN_AUTHOR_ID = "author_uid";
  private static final String COLUMN_AUTHOR_NAME = "author_name";
  private static final String COLUMN_AUTHOR_PHOTO = "author_photo";
  private static final String COLUMN_TEXT = "text";
  private static final String COLUMN_DIRECTION = "direction";
  private static final String COLUMN_DATE = "date";

  private static final String CREATE_TABLE = String.format("create table %s (%s varchar2, %s varchar2, %s varchar2, %s varchar2 not null, %s varchar2 not null, %s integer not null)", TABLE_NAME, COLUMN_AUTHOR_ID, COLUMN_AUTHOR_NAME, COLUMN_AUTHOR_PHOTO, COLUMN_TEXT, COLUMN_DIRECTION, COLUMN_DATE);

  public ChatMessageDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void insert(ChatMessage chatMessage) {
    ContentValues values = new ContentValues();
    Author author = chatMessage.getAuthor();
    if(author != null) {
      values.put(COLUMN_AUTHOR_ID, author.getId());
      values.put(COLUMN_AUTHOR_NAME, author.getName());
      values.put(COLUMN_AUTHOR_PHOTO, author.getPhoto());
    }
    values.put(COLUMN_TEXT, chatMessage.getText());
    values.put(COLUMN_DIRECTION, chatMessage.getDirection().toString());
    values.put(COLUMN_DATE, chatMessage.getDate().getTime());
    SQLiteDatabase database = getWritableDatabase();
    database.insert(TABLE_NAME, null, values);
    database.close();
  }

  public List<ChatMessage> list() {
    String orderBy = COLUMN_DATE + " desc";
    SQLiteDatabase database = getReadableDatabase();
    Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, orderBy);
    List<ChatMessage> messages = new ArrayList<>();
    while(cursor.moveToNext()) {
      Author author = new Author();
      author.setId(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_ID)));
      author.setName(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)));
      author.setPhoto(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_PHOTO)));
      ChatMessage message = new ChatMessage();
      message.setAuthor(author);
      message.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
      message.setDirection(ChatMessage.Direction.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DIRECTION))));
      message.setDate(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
      messages.add(message);
    }
    cursor.close();
    database.close();
    return messages;
  }
}