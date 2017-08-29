package io.chatz.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.chatz.R;
import io.chatz.model.ChatMessage;
import io.chatz.util.ImageUtils;
import io.chatz.util.UIUtils;

public class ChatAdapter extends BaseAdapter<ChatMessage, ChatAdapter.ChatMessageHolder> {

  private static final int OUTGOING_MESSAGE = 0;
  private static final int INCOMING_MESSAGE = 1;
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private int pictureDimension;
  private int errorIconDimension;
  private int conversationMargin;

  public ChatAdapter(Context context) {
    super(context, new ArrayList<ChatMessage>());
    pictureDimension = UIUtils.dpToPixels(getContext(), 45);
    errorIconDimension = UIUtils.dpToPixels(getContext(), 36);
    conversationMargin = UIUtils.dpToPixels(getContext(), 5);
  }

  @Override
  public int getItemViewType(int position) {
    ChatMessage chatMessage = getItem(position);
    return ChatMessage.Direction.outgoing.equals(chatMessage.getDirection()) ? OUTGOING_MESSAGE : INCOMING_MESSAGE;
  }

  @Override
  public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(getContext()).inflate(viewType == OUTGOING_MESSAGE ? R.layout.item_chat_message_outgoing : R.layout.item_chat_message_incoming, parent, false);
    return viewType == OUTGOING_MESSAGE ? new OutgoingMessageHolder(view) : new IncomingMessageHolder(view);
  }

  @Override
  public void onBindViewHolder(ChatMessageHolder holder, int position) {
    ChatMessage chatMessage = getItem(position);
    ChatMessage nextChatMessage = hasItem(position - 1) ? getItem(position - 1) : null;
    holder.timeView.setText(TIME_FORMAT.format(chatMessage.getDate()));

    boolean sameDay = nextChatMessage != null && sameDay(chatMessage.getDate(), nextChatMessage.getDate());
    boolean continuation = nextChatMessage != null && nextChatMessage.getDirection().equals(chatMessage.getDirection()) && (nextChatMessage.getDirection().equals(ChatMessage.Direction.outgoing) || (nextChatMessage.getDirection().equals(ChatMessage.Direction.incoming) && chatMessage.getAuthor().equals(nextChatMessage.getAuthor()))) && sameDay;

    ((RecyclerView.LayoutParams) holder.rootView.getLayoutParams()).topMargin = !continuation ? conversationMargin : 0;

    if (holder instanceof IncomingMessageHolder) {
      IncomingMessageHolder incomingMessageHolder = (IncomingMessageHolder) holder;
      if (!continuation) {
        ImageUtils.setPicture(getContext(), chatMessage.getAuthor().getPhotoUrl(), incomingMessageHolder.photoView);
        ((RelativeLayout.LayoutParams) incomingMessageHolder.photoView.getLayoutParams()).height = pictureDimension;
        incomingMessageHolder.photoView.setVisibility(View.VISIBLE);
        incomingMessageHolder.authorView.setText(chatMessage.getAuthor().getName());
        incomingMessageHolder.authorView.setVisibility(View.VISIBLE);
      } else {
        ((RelativeLayout.LayoutParams) incomingMessageHolder.photoView.getLayoutParams()).height = 0;
        incomingMessageHolder.photoView.setVisibility(View.INVISIBLE);
        incomingMessageHolder.authorView.setVisibility(View.GONE);
      }
      holder.textView.setText(fromHtml(chatMessage.getText() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
    } else {
      OutgoingMessageHolder outgoingMessageHolder = (OutgoingMessageHolder) holder;
      if (chatMessage.getStatus() == null || ChatMessage.Status.sent.equals(chatMessage.getStatus())) {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.errorView.getLayoutParams()).width = 0;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.message_sent);
      } else if (ChatMessage.Status.sending.equals(chatMessage.getStatus())) {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.errorView.getLayoutParams()).width = 0;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.message_sending);
      } else {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.errorView.getLayoutParams()).width = errorIconDimension;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.message_error_sending);
      }
      holder.textView.setText(fromHtml(chatMessage.getText() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
      applyOutgoingColor(outgoingMessageHolder);
    }
  }

  private void applyOutgoingColor(OutgoingMessageHolder holder) {
    Drawable callDrawable = holder.statusView.getDrawable().getConstantState().newDrawable().mutate();
    DrawableCompat.setTint(callDrawable, ContextCompat.getColor(getContext(), R.color.chatz_outgoing_message));
    holder.statusView.setImageDrawable(callDrawable);
  }

  private Spanned fromHtml(String source) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    } else {
      return Html.fromHtml(source);
    }
  }

  private boolean sameDay(Date firstDate, Date secondDate) {
    Calendar firstCalendar = Calendar.getInstance();
    firstCalendar.setTime(firstDate);
    Calendar secondCalendar = Calendar.getInstance();
    secondCalendar.setTime(secondDate);
    return sameDay(firstCalendar, secondCalendar);
  }

  private boolean sameDay(Calendar firstCalendar, Calendar secondCalendar) {
    return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) && firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR);
  }

  private class IncomingMessageHolder extends ChatMessageHolder {

    private TextView authorView;
    private ImageView photoView;

    IncomingMessageHolder(View view) {
      super(view);
      authorView = (TextView) view.findViewById(R.id.author);
      photoView = (ImageView) view.findViewById(R.id.photo);
    }
  }

  private class OutgoingMessageHolder extends ChatMessageHolder {

    private ImageView statusView;
    private ImageView errorView;

    OutgoingMessageHolder(View view) {
      super(view);
      statusView = (ImageView) view.findViewById(R.id.status);
      errorView = (ImageView) view.findViewById(R.id.error);
    }
  }

  abstract class ChatMessageHolder extends RecyclerView.ViewHolder {

    private View rootView;
    private TextView textView;
    private TextView timeView;

    ChatMessageHolder(View view) {
      super(view);
      rootView = view.findViewById(R.id.root);
      textView = (TextView) view.findViewById(R.id.text);
      timeView = (TextView) view.findViewById(R.id.time);
    }
  }
}