package ru.creativityprojectcenter.groupchatapp.presentation.signup;

import dagger.Binds;
import dagger.Module;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;

@Module
public abstract class SignUpModule {

    @ActivityScoped
    @Binds
    abstract SignUpContract.Presenter signUpPresenter(SignUpPresenter presenter);

    @ActivityScoped
    @Binds
    abstract SignUpContract.View signUpView(SignUpActivity signUpActivity);

}