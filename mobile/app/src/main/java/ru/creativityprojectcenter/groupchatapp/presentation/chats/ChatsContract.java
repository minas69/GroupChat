package ru.creativityprojectcenter.groupchatapp.presentation.chats;

import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.presentation.base.IBasePresenter;

public interface ChatsContract {

    interface View {

        void showChats(RealmResults<Chat> chats);

        void showError(String message);

        void setLoadingIndicator(final boolean active);

        void showAddNewChat();

        void showSuccessfullyAddedChat();

    }

    interface Presenter extends IBasePresenter {

        void result(int requestCode, int resultCode);

        void getChats();

        void syncChats();

        void addNewChat();

    }

}
