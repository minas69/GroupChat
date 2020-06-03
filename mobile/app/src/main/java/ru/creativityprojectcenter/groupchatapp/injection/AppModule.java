package ru.creativityprojectcenter.groupchatapp.injection;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

@Module
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

}
