package tomdrever.timetable.data;

import android.databinding.ObservableArrayList;

import java.io.Serializable;

public class Timetable implements Serializable {
    private transient TimetableValueChangedListener valueChangedListener;

    private TimetableValueChangedListener getValueChangedListener() {
        return valueChangedListener;
    }

    public void setValueChangedListener(TimetableValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public Timetable() {
        days = new ObservableArrayList<>();
        valueChangedListener = null;
    }

    public Timetable(TimetableValueChangedListener valueChangedListener) {
        days = new ObservableArrayList<>();
        this.valueChangedListener = valueChangedListener;
    }

    public Timetable(Timetable timetable) {
        days = new ObservableArrayList<>();
        days.addAll(timetable.getDays());
        valueChangedListener = timetable.getValueChangedListener();
    }

    private ObservableArrayList<Day> days;

    public void addDay(Day day){
        days.add(day);
        valueChangedListener.onValueAdded(days.size() - 1);
    }

    public void addDay(Day day, int position) {
        days.add(position, day);
        valueChangedListener.onValueAdded(position);
    }

    public void removeDay(int position) {
        days.remove(position);
        valueChangedListener.onValueRemoved(position);
    }

    public ObservableArrayList<Day> getDays(){
        return days;
    }
}
