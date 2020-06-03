package ru.creativityprojectcenter.groupchatapp.injection.android;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;
import ru.creativityprojectcenter.groupchatapp.presentation.SplashActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.addchat.AddChatActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.addchat.AddChatModule;
import ru.creativityprojectcenter.groupchatapp.presentation.chat.ChatActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.chat.ChatModule;
import ru.creativityprojectcenter.groupchatapp.presentation.chats.ChatsActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.chats.ChatsModule;
import ru.creativityprojectcenter.groupchatapp.presentation.login.LoginActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.login.LoginModule;
import ru.creativityprojectcenter.groupchatapp.presentation.signup.SignUpActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.signup.SignUpModule;

@Module
public abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SignUpModule.class)
    abstract SignUpActivity signUpActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ChatsModule.class)
    abstract ChatsActivity chatsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ChatModule.class)
    abstract ChatActivity chatActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddChatModule.class)
    abstract AddChatActivity addChatActivity();

}
