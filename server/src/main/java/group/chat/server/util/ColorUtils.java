package group.chat.server.util;

import java.util.Random;

public class ColorUtils {
    
    private static String[] colors = {
        Constants.Color.RED,
        Constants.Color.PINK,
        Constants.Color.PURPLE,
        Constants.Color.DEEP_PURPLE,
        Constants.Color.INDIGO,
        Constants.Color.BLUE,
        Constants.Color.LIGHT_BLUE,
        Constants.Color.CYAN,
        Constants.Color.TEAL,
        Constants.Color.GREEN,
        Constants.Color.LIGHT_GREEN,
        Constants.Color.LIME,
        Constants.Color.YELLOW,
        Constants.Color.AMBER,
        Constants.Color.ORANGE,
        Constants.Color.DEEP_ORANGE,
        Constants.Color.BROWN,
        Constants.Color.BLUE_GRAY
    };

    private ColorUtils() {
        throw new IllegalStateException(Constants.INSTANTIATION_NOT_ALLOWED);
    }

    public static String getRandomColor() {
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }
    
}
