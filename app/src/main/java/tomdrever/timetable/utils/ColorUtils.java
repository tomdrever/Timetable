package tomdrever.timetable.utils;

import android.graphics.Color;

public class ColorUtils {
    public static int lighten(int color) {
        return Color.argb(Color.alpha(color) - 110, Color.red(color), Color.green(color), Color.blue(color));
    }
}
