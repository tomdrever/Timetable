package tomdrever.timetable.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import org.joda.time.LocalTime;
import org.joda.time.Interval;
import tomdrever.timetable.data.listeners.DataValueChangedListener;

import java.io.Serializable;
import java.util.Objects;

public class Day extends BaseObservable implements Serializable {
    private ObservableArrayList<Period> periods;

	private transient DataValueChangedListener valueChangedListener;

	public void setValueChangedListener(DataValueChangedListener valueChangedListener) {
		this.valueChangedListener = valueChangedListener;
	}

	private DataValueChangedListener getValueChangedListener() {
		return valueChangedListener;
	}

    @Bindable
    public ObservableArrayList<Period> getPeriods() {
        return periods;
    }

    public void addPeriod(Period period) {
        addPeriod(period, periods.size());
    }

    public void addPeriod(Period period, int position) {
	    // TODO -  reorganise periods on addition or removal to order chronologically
	    periods.add(position, period);
	    valueChangedListener.onValueAdded(position);
    }

    public void removePeriod(int position){
        periods.remove(position);
	    valueChangedListener.onValueRemoved(position);
    }

    private String name;

    @Bindable
    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

	public Day(String name) {
		this.name = name;
		this.valueChangedListener = null;
		periods = new ObservableArrayList<>();
	}

    public Day(String name, DataValueChangedListener valueChangedListener) {
        this.name = name;
	    this.valueChangedListener = valueChangedListener;
        periods = new ObservableArrayList<>();
    }

    public Day(Day day) {
        name = day.getName();
	    valueChangedListener = day.getValueChangedListener();

        periods = new ObservableArrayList<>();
	    for (int i = 0; i < day.getPeriods().size(); i++) {
		    periods.add(new Period(day.getPeriods().get(i)));
	    }
    }


    @Bindable
    public LocalTime getStartTime(){
        return periods.get(0).getStartTime();
    }

    @Bindable
    public LocalTime getEndTime(){
        return  periods.get(periods.size() - 1).getEndTime();
    }

	public Period getPeriodAt(LocalTime time) {

		// Preliminary "is within day" checks
		if (new Interval(getStartTime().toDateTimeToday(), getEndTime().toDateTimeToday()).contains(time.toDateTimeToday()))
			return null;

		// Will stop at the first instance - there should only be one tho
		for (Period period : periods) {
			if (period.getTimeSpan().contains(time.toDateTimeToday()))
				return period;
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Day) {
			Day other = (Day) obj;

			return Objects.equals(other.name, name) && other.periods.size() == periods.size() &&
					other.periods.equals(periods);
		}

		return super.equals(obj);
	}
}
