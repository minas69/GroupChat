package ru.creativityprojectcenter.groupchatapp.data.net.model;

import com.google.gson.annotations.Expose;

public class IsMemberResponse {

    @Expose
    private boolean isMember;

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}
