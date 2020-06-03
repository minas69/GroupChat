package ru.creativityprojectcenter.groupchatapp.util;

import android.content.Context;
import android.support.annotation.ColorInt;

import ru.creativityprojectcenter.groupchatapp.R;

public class ColorUtils {

    @ColorInt
    public static int getColorIntByName(Context context, String color) {
        switch (color) {
            case Constants.Color.RED:
                return context.getResources().getColor(R.color.colorRed);
            case Constants.Color.PINK:
                return context.getResources().getColor(R.color.colorPink);
            case Constants.Color.PURPLE:
                return context.getResources().getColor(R.color.colorPurple);
            case Constants.Color.DEEP_PURPLE:
                return context.getResources().getColor(R.color.colorDeepPurple);
            case Constants.Color.INDIGO:
                return context.getResources().getColor(R.color.colorIndigo);
            case Constants.Color.BLUE:
                return context.getResources().getColor(R.color.colorBlue);
            case Constants.Color.LIGHT_BLUE:
                return context.getResources().getColor(R.color.colorLightBlue);
            case Constants.Color.CYAN:
                return context.getResources().getColor(R.color.colorCyan);
            case Constants.Color.TEAL:
                return context.getResources().getColor(R.color.colorTeal);
            case Constants.Color.GREEN:
                return context.getResources().getColor(R.color.colorGreen);
            case Constants.Color.LIGHT_GREEN:
                return context.getResources().getColor(R.color.colorLightGreen);
            case Constants.Color.LIME:
                return context.getResources().getColor(R.color.colorLime);
            case Constants.Color.YELLOW:
                return context.getResources().getColor(R.color.colorYellow);
            case Constants.Color.AMBER:
                return context.getResources().getColor(R.color.colorAmber);
            case Constants.Color.ORANGE:
                return context.getResources().getColor(R.color.colorOrange);
            case Constants.Color.DEEP_ORANGE:
                return context.getResources().getColor(R.color.colorDeepOrange);
            case Constants.Color.BROWN:
                return context.getResources().getColor(R.color.colorBrown);
            default:
                return context.getResources().getColor(R.color.colorBlueGray);
        }
    }
    
}
