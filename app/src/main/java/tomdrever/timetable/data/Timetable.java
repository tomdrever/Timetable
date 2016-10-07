package tomdrever.timetable.data;

import android.databinding.ObservableArrayList;

import java.io.Serializable;

public class Timetable implements Serializable {
    private transient DataValueChangedListener valueChangedListener;

    private DataValueChangedListener getValueChangedListener() {
        return valueChangedListener;
    }

    public void setValueChangedListener(DataValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public Timetable() {
        days = new ObservableArrayList<>();
        valueChangedListener = null;
    }

    public Timetable(DataValueChangedListener valueChangedListener) {
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
        addDay(day, days.size());
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timetable) {
            Timetable other = (Timetable) obj;
            return other.getDays().equals(days);
        }

        return super.equals(obj);
    }
}
