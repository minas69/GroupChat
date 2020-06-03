package ru.creativityprojectcenter.groupchatapp.presentation.chats;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.util.ColorUtils;

public class ChatsAdapter
        extends RealmRecyclerViewAdapter<Chat, ChatsAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private Context context;
    private OnClickListener listener;

    ChatsAdapter(Context context, OrderedRealmCollection<Chat> data) {
        super(data, true);
        this.context = context;
    }

    void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Chat chat = getData().get(position);
        holder.bind(chat);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.chat_profile) View profile;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.last_message_layout) LinearLayout messageLayout;
        @BindView(R.id.nick_name) TextView nickName;
        @BindView(R.id.last_message) TextView message;
        @BindView(R.id.date) TextView date;

        ViewHolder(View v) {
            super(v);
            view = v;
            ButterKnife.bind(this, v);
        }

        void bind(Chat chat) {
            title.setText(chat.getTitle());

            GradientDrawable drawable = (GradientDrawable) profile.getBackground();
            drawable.setColor(ColorUtils.getColorIntByName(context, chat.getColor()));

            Message lastMessage = chat.getLastMessage();
            if (lastMessage != null) {
                messageLayout.setVisibility(View.VISIBLE);
                if (lastMessage.getSender() != null) {
                    nickName.setText(lastMessage.getSender().getNickName() + ": ");
                    nickName.setVisibility(View.VISIBLE);
                } else {
                    nickName.setText("");
                    nickName.setVisibility(View.INVISIBLE);
                }
                message.setText(lastMessage.getBody());

                SimpleDateFormat format = new SimpleDateFormat("dd MMM", Locale.getDefault());
                date.setText(format.format(lastMessage.getDate()));
            } else {
                messageLayout.setVisibility(View.INVISIBLE);
            }

            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChatClicked(chat);
                }
            });
        }
    }

    public interface OnClickListener {
        void onChatClicked(Chat chat);
    }

}
