package ru.creativityprojectcenter.groupchatapp.data.net;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;

public interface MessageService {

    @GET("api/messages")
    Observable<List<Message>> getMessages(@Query("chatId") int chatId);

}
