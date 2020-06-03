package ru.creativityprojectcenter.groupchatapp.presentation.login;

import ru.creativityprojectcenter.groupchatapp.presentation.base.IBasePresenter;

public interface LoginContract {

    interface View {

        void navigateToChats();

        void navigateToSignUpPage();

        void setLoadingIndicator(boolean active);

        void showError(String message);

        void nicknameIncorrect();

        void passwordIncorrect();

    }

    interface Presenter extends IBasePresenter {

        void attemptLogin(String nickname, String password, boolean rememberPassword);

    }

}
