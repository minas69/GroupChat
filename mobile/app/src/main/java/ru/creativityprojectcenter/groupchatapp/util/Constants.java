package ru.creativityprojectcenter.groupchatapp.util;

public final class Constants {

    private static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";

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
    
    final class Color {
        
        private Color() {
            throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
        }

        static final String RED = "RED";
        static final String PINK = "PINK";
        static final String PURPLE = "PURPLE";
        static final String DEEP_PURPLE = "DEEP_PURPLE";
        static final String INDIGO = "INDIGO";
        static final String BLUE = "BLUE";
        static final String LIGHT_BLUE = "LIGHT_BLUE";
        static final String CYAN = "CYAN";
        static final String TEAL = "TEAL";
        static final String GREEN = "GREEN";
        static final String LIGHT_GREEN = "LIGHT_GREEN";
        static final String LIME = "LIME";
        static final String YELLOW = "YELLOW";
        static final String AMBER = "AMBER";
        static final String ORANGE = "ORANGE";
        static final String DEEP_ORANGE = "DEEP_ORANGE";
        static final String BROWN = "BROWN";
        static final String BLUE_GRAY = "BLUE_GRAY";
        
    }
    
}