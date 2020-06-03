package ru.creativityprojectcenter.groupchatapp.data.net;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class AuthHeaderInterceptor implements Interceptor {

    private Preferences pref;

    public AuthHeaderInterceptor(Preferences pref) {
        this.pref = pref;
    }
    
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String token = pref.getToken();
        if (token != null) {
            request = request
                    .newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }

        return chain.proceed(request);
    }
}
