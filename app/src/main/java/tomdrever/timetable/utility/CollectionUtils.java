package tomdrever.timetable.utility;

import java.util.ArrayList;

import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;

public class CollectionUtils {
    public static ArrayList<Period> copyPeriods(ArrayList<Period> periods) {
        ArrayList<Period> newPeriods = new ArrayList<>();

        for (Period period : periods) {
            newPeriods.add(new Period(period));
        }

        return newPeriods;
    }

    public static ArrayList<Day> copyDays(ArrayList<Day> days) {
        ArrayList<Day> newDays = new ArrayList<>();

        for (Day day : days) {
            newDays.add(new Day(day));
        }

        return newDays;
    }
}
