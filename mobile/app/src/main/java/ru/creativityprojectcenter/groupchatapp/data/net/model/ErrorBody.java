package ru.creativityprojectcenter.groupchatapp.data.net.model;

public class ErrorBody extends Throwable {

    private int code;

    private String message;

    public ErrorBody(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
