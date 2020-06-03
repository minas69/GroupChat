package ru.creativityprojectcenter.groupchatapp.data.net;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.creativityprojectcenter.groupchatapp.data.net.model.TokenResponse;

public interface AccountService {

    @FormUrlEncoded
    @POST("login")
    Observable<TokenResponse> login(@Field("nickname") String nickname,
                                    @Field("pass") String pass);

    @FormUrlEncoded
    @POST("signUp")
    Observable<TokenResponse> signUp(@Field("nickname") String nickname,
                                       @Field("email") String email,
                                       @Field("pass") String pass);

}
