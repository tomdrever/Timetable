package tomdrever.timetable.utils;

import java.util.ArrayList;

import tomdrever.timetable.data.DataItem;

public class CollectionUtils {

    public static <T extends DataItem> ArrayList<T> copy(ArrayList<T> items) {
        ArrayList<T> newItems = new ArrayList<>();

        for (T item : items) {
            newItems.add((T)item.cloneItem());
        }

        return newItems;
    }
}
