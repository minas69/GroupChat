package ru.creativityprojectcenter.groupchatapp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.presentation.chats.ChatsActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.login.LoginActivity;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class SplashActivity extends AppCompatActivity {

    @Inject
    Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Intent i;
        if (pref.isSessionRemembered() && pref.getToken() != null) {
            i = new Intent(this, ChatsActivity.class);
        } else {
            pref.setToken(null);
            i = new Intent(this, LoginActivity.class);
        }

        startActivity(i);
        finish();
    }
}
