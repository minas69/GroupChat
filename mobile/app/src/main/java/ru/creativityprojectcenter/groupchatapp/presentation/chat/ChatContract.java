package ru.creativityprojectcenter.groupchatapp.presentation.chat;

import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.presentation.base.IBasePresenter;
import ru.creativityprojectcenter.groupchatapp.service.IMessageService;

public interface ChatContract {

    interface View {

        void showMessages(RealmResults<Message> messages);

        void showError(String message);

        void setLoadingIndicator(final boolean active);

        void setMessageInput(boolean active);

        void showChatTitle(String title);

        void showChatMembersCount(int count);

    }

    interface Presenter extends IBasePresenter {

        void setChat(int chatId);

        void getMessages();

        void syncMessages();

        void sendMessage(String text);

        void serviceConnected(IMessageService service);

        void serviceDisconnected();

        void joinChat();

        void leaveChat();

    }

}
