package ru.creativityprojectcenter.groupchatapp.presentation.chats;

import dagger.Binds;
import dagger.Module;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;

@Module
public abstract class ChatsModule {

    @ActivityScoped
    @Binds
    abstract ChatsContract.Presenter chatsPresenter(ChatsPresenter presenter);

    @ActivityScoped
    @Binds
    abstract ChatsContract.View chatsView(ChatsActivity chatsActivity);

}