package ru.creativityprojectcenter.groupchatapp.presentation.addchat;

import dagger.Binds;
import dagger.Module;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;

@Module
public abstract class AddChatModule {

    @ActivityScoped
    @Binds
    abstract AddChatContract.Presenter addChatPresenter(AddChatPresenter presenter);

    @ActivityScoped
    @Binds
    abstract AddChatContract.View addChatView(AddChatActivity addChatActivity);

}