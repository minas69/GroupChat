package ru.creativityprojectcenter.groupchatapp.data.net;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.net.model.IsMemberResponse;

public interface ChatService {

    @GET("api/chats")
    Observable<List<Chat>> getChats();

    @FormUrlEncoded
    @POST("api/chat/new")
    Observable<Chat> addChat(@Field("title") String title,
                             @Field("description") String description,
                             @Field("isEphemeral") boolean isEphemeral);

    @PUT("api/chat/join")
    Completable joinChat(@Query("chatId") int chatId);

    @GET("api/chat/members")
    Observable<IsMemberResponse> isJoined(@Query("chatId") int chatId);

}
