package ru.creativityprojectcenter.groupchatapp.presentation.chat;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.util.ColorUtils;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class MessagesAdapter extends RealmRecyclerViewAdapter<Message, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_ADMIN = 3;

    private Context context;
    private Preferences pref;

    MessagesAdapter(Context context, Preferences pref,
                    @Nullable OrderedRealmCollection<Message> data) {
        super(data, true);
        this.context = context;
        this.pref = pref;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getData().get(position);

        if (message.getMessageType().equals(Message.TEXT)) {
            if (message.getSender().getId() == pref.getUserId()) {
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        } else {
            return VIEW_TYPE_MESSAGE_ADMIN;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sent_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_message, parent, false);
            return new ReceivedMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.admin_message, parent, false);
            return new AdminMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = getData().get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_ADMIN:
                ((AdminMessageHolder) holder).bind(message);
        }
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_body) TextView body;
        @BindView(R.id.message_time) TextView time;

        SentMessageHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bind(Message message) {
            body.setText(message.getBody());

            DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
            time.setText(df.format(message.getDate()));
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar) View avatar;
        @BindView(R.id.message_body) TextView body;
        @BindView(R.id.name) TextView nickName;
        @BindView(R.id.message_time) TextView time;

        ReceivedMessageHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bind(Message message) {
            GradientDrawable drawable = (GradientDrawable) avatar.getBackground();
            drawable.setColor(ColorUtils.getColorIntByName(context, message.getSender().getColor()));

            body.setText(message.getBody());
            nickName.setText(message.getSender().getNickName());

            DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
            time.setText(df.format(message.getDate()));
        }
    }

    class AdminMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_body) TextView body;

        AdminMessageHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        void bind(Message message) {
            body.setText(message.getBody());
        }
    }

}
