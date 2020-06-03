package ru.creativityprojectcenter.groupchatapp.presentation.addchat;

import ru.creativityprojectcenter.groupchatapp.presentation.base.IBasePresenter;

public interface AddChatContract {

    interface View {

        void showChatsList();

        void showError(String message);

        void setLoadingIndicator(final boolean active);

    }

    interface Presenter extends IBasePresenter {

        void addChat(String title, String description, boolean isEphemeral);

    }

}
