package tomdrever.timetable.data;

import android.databinding.ObservableArrayList;
import tomdrever.timetable.data.listeners.DataValueChangedListener;

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
	    valueChangedListener = timetable.getValueChangedListener();

	    days = new ObservableArrayList<>();
	    for (int i = 0; i < timetable.getDays().size(); i++) {
		    days.add(new Day(timetable.getDays().get(i)));
	    }
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
            return other.days.equals(days) && other.days.size() == days.size();
        }

        return super.equals(obj);
    }
}
