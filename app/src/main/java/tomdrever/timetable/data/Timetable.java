package tomdrever.timetable.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Timetable implements Serializable {
    private transient TimetableValueChangedListener valueChangedListener;

    private TimetableValueChangedListener getValueChangedListener() {
        return valueChangedListener;
    }

    public void setValueChangedListener(TimetableValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public Timetable() {
        days = new ArrayList<>();
        valueChangedListener = null;
    }

    public Timetable(TimetableValueChangedListener valueChangedListener) {
        days = new ArrayList<>();
        this.valueChangedListener = valueChangedListener;
    }

    public Timetable(Timetable timetable) {
        days = new ArrayList<>(timetable.getDays());
        valueChangedListener = timetable.getValueChangedListener();
    }

    private ArrayList<Day> days;

    public void addDay(Day day){
        days.add(day);
        valueChangedListener.onValueChanged(days.size() - 1);
    }

    public void addDay(Day day, int position) {
        days.add(position, day);
        valueChangedListener.onValueChanged(position);
    }

    public void removeDay(int position) {
        days.remove(position);
        valueChangedListener.onValueChanged(position);
    }

    public ArrayList<Day> getDays(){
        return days;
    }
}
