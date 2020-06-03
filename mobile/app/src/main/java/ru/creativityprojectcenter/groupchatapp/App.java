package ru.creativityprojectcenter.groupchatapp;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.creativityprojectcenter.groupchatapp.injection.DaggerAppComponent;

public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

}
