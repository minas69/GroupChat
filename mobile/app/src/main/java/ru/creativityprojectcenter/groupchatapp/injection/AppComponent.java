package ru.creativityprojectcenter.groupchatapp.injection;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.creativityprojectcenter.groupchatapp.App;
import ru.creativityprojectcenter.groupchatapp.data.AccountRepository;
import ru.creativityprojectcenter.groupchatapp.injection.android.ActivityBuilderModule;
import ru.creativityprojectcenter.groupchatapp.injection.android.NetModule;
import ru.creativityprojectcenter.groupchatapp.injection.android.ServiceBuilderModule;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

@Singleton
@Component(modules = {NetModule.class,
        AppModule.class,
        ActivityBuilderModule.class,
        ServiceBuilderModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<App> {

    Preferences getPreferences();

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }

}
