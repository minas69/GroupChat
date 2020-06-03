package ru.creativityprojectcenter.groupchatapp.injection.android;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.creativityprojectcenter.groupchatapp.service.MessageService;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector()
    abstract MessageService messageService();

}
