package group.chat.server.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NicknameValidator {

    private static final Pattern VALID_NICKNAME_REGEX =
            Pattern.compile("^[a-zA-Zа-яА-Я]\\w*$", Pattern.UNICODE_CHARACTER_CLASS);

    public static boolean validate(String nickname) {
        Matcher matcher = VALID_NICKNAME_REGEX.matcher(nickname);
        return matcher.find();
    }

}
