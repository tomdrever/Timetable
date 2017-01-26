package tomdrever.timetable.utils;

import java.util.ArrayList;
import java.util.Collections;

import tomdrever.timetable.data.DataItem;
import tomdrever.timetable.data.Period;

public class CollectionUtils {

    public static <T extends DataItem> ArrayList<T> copy(ArrayList<T> items) {
        ArrayList<T> newItems = new ArrayList<>();

        for (T item : items) {
            newItems.add((T)item.cloneItem());
        }

        return newItems;
    }

    public static void sortPeriods(ArrayList<Period> periods) {
        Collections.sort(periods, new PeriodComparer());
    }
}
