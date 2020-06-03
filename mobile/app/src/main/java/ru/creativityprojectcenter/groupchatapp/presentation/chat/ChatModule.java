package ru.creativityprojectcenter.groupchatapp.presentation.chat;

import dagger.Binds;
import dagger.Module;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;

@Module
public abstract class ChatModule {

    @ActivityScoped
    @Binds
    abstract ChatContract.Presenter chatPresenter(ChatPresenter presenter);

    @ActivityScoped
    @Binds
    abstract ChatContract.View chatView(ChatActivity chatActivity);

}