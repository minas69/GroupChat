package ru.creativityprojectcenter.groupchatapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Preferences {

    private final SharedPreferences pref;

    @Inject
    public Preferences(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserId(int userId) {
        pref.edit()
                .putInt("userId", userId)
                .apply();
    }

    public int getUserId() {
        return pref.getInt("userId", 0);
    }

    public int getLocalMessageId() {
        int messageId =  pref.getInt("localMessageId", 0);
        pref.edit()
                .putInt("localMessageId", --messageId)
                .apply();
        return messageId;
    }

    public void setToken(String token) {
        pref.edit()
                .putString("token", token)
                .apply();
    }

    public String getToken() {
        return pref.getString("token", null);
    }

    public void setIsSessionRemembered(boolean active) {
        pref.edit()
                .putBoolean("isSessionRemembered", active)
                .apply();
    }

    public boolean isSessionRemembered() {
        return pref.getBoolean("isSessionRemembered", false);
    }

}
