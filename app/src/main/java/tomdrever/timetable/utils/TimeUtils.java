package tomdrever.timetable.utils;

import java.util.Locale;

public class TimeUtils {
    public static String formatTime(int hour, int minute) {
        boolean am = hour < 12;

        if (hour > 12) hour -= 12;

        String minuteString = String.valueOf(minute);
        if (minute < 10) minuteString = "0" + minuteString;

        return String.format(Locale.ENGLISH, "%d:%s ", hour, minuteString) + (am ? "A.M." : "P.M.");
    }
}
