package io.ayro.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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

import io.ayro.R;
import io.ayro.core.AyroApp;
import io.ayro.model.ChatMessage;
import io.ayro.model.Integration;
import io.ayro.util.ImageUtils;
import io.ayro.util.UIUtils;

public class ChatAdapter extends BaseAdapter<ChatMessage, ChatAdapter.ChatMessageHolder> {

  private static final int OUTGOING_MESSAGE = 0;
  private static final int INCOMING_MESSAGE = 1;
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private int outgoingCardColor;
  private int pictureDimension;
  private int errorIconDimension;
  private int conversationMargin;
  private OnRetryMessageClickListener onRetryMessageClickListener;

  public ChatAdapter(Context context) {
    super(context, new ArrayList<ChatMessage>());
    pictureDimension = UIUtils.dpToPixels(getContext(), 45);
    errorIconDimension = UIUtils.dpToPixels(getContext(), 36);
    conversationMargin = UIUtils.dpToPixels(getContext(), 5);
    String colorHex = AyroApp.getInstance(getContext()).getIntegration().getConfiguration().get(Integration.CONVERSATION_COLOR_CONFIGURATION);
    outgoingCardColor = Color.parseColor(colorHex);
  }

  public void setOnRetryMessageClickListener(OnRetryMessageClickListener onRetryMessageClickListener) {
    this.onRetryMessageClickListener = onRetryMessageClickListener;
  }

  @Override
  public int getItemViewType(int position) {
    ChatMessage chatMessage = getItem(position);
    return ChatMessage.Direction.outgoing.equals(chatMessage.getDirection()) ? OUTGOING_MESSAGE : INCOMING_MESSAGE;
  }

  @Override
  public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(getContext()).inflate(viewType == OUTGOING_MESSAGE ? R.layout.ayro_item_chat_message_outgoing : R.layout.ayro_item_chat_message_incoming, parent, false);
    return viewType == OUTGOING_MESSAGE ? new OutgoingMessageHolder(view) : new IncomingMessageHolder(view);
  }

  @Override
  public void onBindViewHolder(ChatMessageHolder holder, int position) {
    ChatMessage chatMessage = getItem(position);
    ChatMessage nextChatMessage = hasItem(position - 1) ? getItem(position - 1) : null;
    holder.timeView.setText(TIME_FORMAT.format(chatMessage.getDate()));

    boolean sameDay = nextChatMessage != null && sameDay(chatMessage.getDate(), nextChatMessage.getDate());
    boolean continuation = nextChatMessage != null && nextChatMessage.getDirection().equals(chatMessage.getDirection()) && (nextChatMessage.getDirection().equals(ChatMessage.Direction.outgoing) || (nextChatMessage.getDirection().equals(ChatMessage.Direction.incoming) && chatMessage.getAgent().equals(nextChatMessage.getAgent()))) && sameDay;

    ((RecyclerView.LayoutParams) holder.rootView.getLayoutParams()).topMargin = !continuation ? conversationMargin : 0;

    if (holder instanceof IncomingMessageHolder) {
      IncomingMessageHolder incomingMessageHolder = (IncomingMessageHolder) holder;
      if (!continuation) {
        ImageUtils.setPicture(getContext(), chatMessage.getAgent().getPhotoUrl(), incomingMessageHolder.photoView);
        ((RelativeLayout.LayoutParams) incomingMessageHolder.photoView.getLayoutParams()).height = pictureDimension;
        incomingMessageHolder.photoView.setVisibility(View.VISIBLE);
        incomingMessageHolder.agentView.setText(chatMessage.getAgent().getName());
        incomingMessageHolder.agentView.setVisibility(View.VISIBLE);
      } else {
        ((RelativeLayout.LayoutParams) incomingMessageHolder.photoView.getLayoutParams()).height = 0;
        incomingMessageHolder.photoView.setVisibility(View.INVISIBLE);
        incomingMessageHolder.agentView.setVisibility(View.GONE);
      }
      holder.textView.setText(fromHtml(chatMessage.getText() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
    } else {
      OutgoingMessageHolder outgoingMessageHolder = (OutgoingMessageHolder) holder;
      outgoingMessageHolder.cardView.setCardBackgroundColor(outgoingCardColor);
      if (chatMessage.getStatus() == null || ChatMessage.Status.sent.equals(chatMessage.getStatus())) {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.retryView.getLayoutParams()).width = 0;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.ayro_message_sent);
      } else if (ChatMessage.Status.sending.equals(chatMessage.getStatus())) {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.retryView.getLayoutParams()).width = 0;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.ayro_message_sending);
      } else {
        ((RelativeLayout.LayoutParams) outgoingMessageHolder.retryView.getLayoutParams()).width = errorIconDimension;
        outgoingMessageHolder.statusView.setImageResource(R.drawable.ayro_message_error);
      }
      holder.textView.setText(fromHtml(chatMessage.getText() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
    }
  }

  private Spanned fromHtml(String source) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
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

    private TextView agentView;
    private ImageView photoView;

    IncomingMessageHolder(View view) {
      super(view);
      agentView = (TextView) view.findViewById(R.id.agent);
      photoView = (ImageView) view.findViewById(R.id.photo);
    }
  }

  private class OutgoingMessageHolder extends ChatMessageHolder {

    private ImageView statusView;
    private ImageView retryView;
    private CardView cardView;

    OutgoingMessageHolder(View view) {
      super(view);
      cardView = (CardView) view.findViewById(R.id.card);
      statusView = (ImageView) view.findViewById(R.id.status);
      retryView = (ImageView) view.findViewById(R.id.retry);
      retryView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (onRetryMessageClickListener != null) {
            int adapterPosition = getAdapterPosition();
            onRetryMessageClickListener.onClick(adapterPosition, getItem(adapterPosition));
          }
        }
      });
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

  public interface OnRetryMessageClickListener {

    void onClick(int position, ChatMessage chatMessage);

  }
}
