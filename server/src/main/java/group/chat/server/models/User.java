package group.chat.server.models;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class User implements Serializable {

    private int id;

    private String email;

    private String color;

    private Credentials credentials;

    public User() {
        this.credentials = new Credentials();
    }

    public User(int id, String nickname, String email, String password, String color) {
        this.id = id;
        this.email = email;
        this.color = color;
        this.credentials = new Credentials(nickname, password);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Boolean passwordEquals(String password){
        return BCrypt.checkpw(password, this.credentials.getPassword());
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Credentials getCredentials() {
        return credentials;
    }
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
