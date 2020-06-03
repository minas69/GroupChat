package group.chat.server.models;

import org.mindrot.jbcrypt.BCrypt;

public class Credentials {

    private String nickname;

    private String password;

    public Credentials() {}

    public Credentials(String nickname,
                       String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));;
    }
    public String getPassword() {
        return password;
    }

    public Boolean passwordEquals(String password){
        return BCrypt.checkpw(password, this.password);
    }

}