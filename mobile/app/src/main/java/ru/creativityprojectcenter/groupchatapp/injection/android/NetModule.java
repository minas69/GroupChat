package ru.creativityprojectcenter.groupchatapp.injection.android;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.creativityprojectcenter.groupchatapp.BuildConfig;
import ru.creativityprojectcenter.groupchatapp.data.net.AccountService;
import ru.creativityprojectcenter.groupchatapp.data.net.AuthHeaderInterceptor;
import ru.creativityprojectcenter.groupchatapp.data.net.ChatService;
import ru.creativityprojectcenter.groupchatapp.data.net.MessageService;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

@Module
public class NetModule {

    @Provides
    @Singleton
    MessageService provideMessageService(Retrofit retrofit) {
        return retrofit.create(MessageService.class);
    }

    @Provides
    @Singleton
    AccountService provideAccountService(Retrofit retrofit) {
        return retrofit.create(AccountService.class);
    }

    @Provides
    @Singleton
    ChatService provideChatService(Retrofit retrofit) {
        return retrofit.create(ChatService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Preferences preferences) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(new AuthHeaderInterceptor(preferences))
                .addInterceptor(loggingInterceptor)
                .build();
    }

}
