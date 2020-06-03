package ru.creativityprojectcenter.groupchatapp.presentation.signup;

import ru.creativityprojectcenter.groupchatapp.presentation.base.IBasePresenter;

public interface SignUpContract {

    interface View {

        void navigateToChats();

        void setLoadingIndicator(boolean active);

        void showError(String message);

        void nicknameIncorrect();

        void emailIncorrect();

        void passwordIncorrect();

    }

    interface Presenter extends IBasePresenter {

        void attemptSignUp(String nickname,
                           String password,
                           String email,
                           boolean rememberPassword);

    }

}
