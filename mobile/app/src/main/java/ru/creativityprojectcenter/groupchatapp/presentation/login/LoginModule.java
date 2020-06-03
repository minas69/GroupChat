package ru.creativityprojectcenter.groupchatapp.presentation.login;

import dagger.Binds;
import dagger.Module;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;

@Module
public abstract class LoginModule {

    @ActivityScoped
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);

    @ActivityScoped
    @Binds
    abstract LoginContract.View loginView(LoginActivity loginActivity);

}