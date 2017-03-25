package tomdrever.timetable.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Day extends DataItem<Day>{
    private ArrayList<Period> periods;

    public ArrayList<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(ArrayList<Period> periods) {
        this.periods = periods;
    }

    public void addPeriod(Period period) {
        addPeriod(period, periods.size());
    }

    public void addPeriod(Period period, int position) {
		// collections.sort
		// period.compare, based on final time?
	    periods.add(position, period);
    }

    public void removePeriod(int position){
        periods.remove(position);
    }

    private String name;

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

	public Day() {
        periods = new ArrayList<>();
    }

    public Day(Parcel in) {
        name = in.readString();
        periods = new ArrayList<Period>(Arrays.asList(
                (Period[])in.readParcelableArray(Period.class.getClassLoader())));
        colour = in.readInt();
    }

	public Day(String name) {
		this.name = name;
        periods = new ArrayList<>();
	}

    public LocalTime getStartTime(){
        return periods.get(0).getStartTime();
    }

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

    private int colour;

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
	public boolean equals(Object obj) {
		if (obj instanceof Day) {
			Day other = (Day) obj;

			return Objects.equals(other.name, name) && other.periods.size() == periods.size() &&
					other.periods.equals(periods) && other.colour == colour;
		}

		return super.equals(obj);
	}

    @Override
    public Day cloneItem() {
        Day day = new Day();

        day.name = name;

        for (int i = 0; i < periods.size(); i++) {
            day.periods.add(periods.get(i).cloneItem());
        }

        day.colour = colour;

        return day;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeParcelableArray(periods.toArray(new Period[1]), flags);
        out.writeInt(colour);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
