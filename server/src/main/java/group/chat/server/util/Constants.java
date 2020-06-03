package group.chat.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Constants {

    public static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";
    public static final String CHAT_ID_KEY = "chatId";
    public static final String USER_NAME_KEY = "userName";
    public static final String BODY_KEY = "body";
    public static final String AUTHOR_KEY = "author";
    public static final String USER_KEY = "user";
    public static final String ACCESS_TOKEN_KEY = "accessToken";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private Constants() {
        throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
    }

    public final class Code {

        private Code() {
            throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
        }

        public static final int INCORRECT_NICKNAME = 400101;
        public static final int INCORRECT_PASSWORD = 400102;
        public static final int INCORRECT_EMAIL = 400103;

    }

    public final class Color {

        private Color() {
            throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
        }

        public static final String RED = "RED";
        public static final String PINK = "PINK";
        public static final String PURPLE = "PURPLE";
        public static final String DEEP_PURPLE = "DEEP_PURPLE";
        public static final String INDIGO = "INDIGO";
        public static final String BLUE = "BLUE";
        public static final String LIGHT_BLUE = "LIGHT_BLUE";
        public static final String CYAN = "CYAN";
        public static final String TEAL = "TEAL";
        public static final String GREEN = "GREEN";
        public static final String LIGHT_GREEN = "LIGHT_GREEN";
        public static final String LIME = "LIME";
        public static final String YELLOW = "YELLOW";
        public static final String AMBER = "AMBER";
        public static final String ORANGE = "ORANGE";
        public static final String DEEP_ORANGE = "DEEP_ORANGE";
        public static final String BROWN = "BROWN";
        public static final String BLUE_GRAY = "BLUE_GRAY";

    }
}