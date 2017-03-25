package tomdrever.timetable.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.Objects;

public class Period extends DataItem<Period> {
    public Period() {}

    public Period(Parcel in) {
        name = in.readString();
        startTime = (LocalTime) in.readValue(LocalTime.class.getClassLoader());
        endTime = (LocalTime) in.readValue(LocalTime.class.getClassLoader());
        colour = in.readInt();
    }

    public Period(String name) {
        this.name = name;
    }

    private String name;

    public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}

    private LocalTime startTime;
    public void setStartTime(LocalTime startTime){
        this.startTime = startTime;
    }
    public LocalTime getStartTime(){
        return startTime;
    }

    private LocalTime endTime;
    public void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }
    public LocalTime getEndTime(){
        return endTime;
    }

    public Interval getTimeSpan() {
        return new Interval(startTime.toDateTimeToday(), endTime.toDateTimeToday());
    }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Period) {
			Period other = (Period) obj;

			return Objects.equals(other.name, name)
                    && other.startTime.equals(startTime) && other.endTime.equals(endTime)
                    && other.colour == colour;
		}

		return super.equals(obj);
	}

	@Override
	public Period cloneItem() {
		Period period = new Period();

        period.name = name;
        period.startTime = startTime;
        period.endTime = endTime;
		period.colour = colour;

		return period;
	}

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeValue(startTime);
        out.writeValue(endTime);
        out.writeInt(colour);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() {
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        public Period[] newArray(int size) {
            return new Period[size];
        }
    };

    private int colour;

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}
}
