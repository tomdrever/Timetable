package tomdrever.timetable.utils;

import android.graphics.Color;

public class ColourUtils {
    public static int lighten(int color) {
        return Color.argb(Color.alpha(color) - 110, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int green = Color.rgb(67,160,71);
}
