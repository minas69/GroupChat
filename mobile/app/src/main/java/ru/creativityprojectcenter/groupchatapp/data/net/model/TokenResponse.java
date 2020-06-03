package ru.creativityprojectcenter.groupchatapp.data.net.model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("userId")
    private int userId;

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
