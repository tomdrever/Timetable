package tomdrever.timetable.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Timetable extends DataItem<Timetable> implements Comparable<Timetable> {
    private String name;
    private String description;
    private ArrayList<Day> days;
    private int index;
    private String id;

    public Timetable() {
        days = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public Timetable(Parcel in) {
        id = in.readString();
        name = in.readString();
        index = in.readInt();
        description = in.readString();
        days = new ArrayList<>();
        Object[] inDays = in.readArray(Period.class.getClassLoader());
        for (Object day : inDays) {
            if (day instanceof Day) {
                days.add((Day) day);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDay(Day day){
        addDay(day, days.size());
    }

    public void addDay(Day day, int position) {
        days.add(position, day);
    }

    public void removeDay(int position) {
        days.remove(position);
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public ArrayList<Day> getDays(){
        return days;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isBlank() {
        return (name == null || name.equals("")) && (description == null || description.equals("")) && days.size() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timetable) {
            Timetable other = (Timetable) obj;

            return Objects.equals(other.id, id) && other.days.size() == days.size() &&
                    other.days.equals(days);
        }

        return super.equals(obj);
    }

    @Override
    public int compareTo(Timetable timetable) {
        return Integer.compare(index, timetable.index);
    }

    @Override
    public Timetable cloneItem() {
        Timetable timetable = new Timetable();
        timetable.id = id;
        timetable.name = name;
        timetable.index = index;
        timetable.description = description;

        for (int i = 0; i < days.size(); i++) {
            timetable.days.add(days.get(i).cloneItem());
        }

        return timetable;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeInt(index);
        out.writeString(description);
        out.writeArray(days.toArray());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Timetable> CREATOR = new Parcelable.Creator<Timetable>() {
        public Timetable createFromParcel(Parcel in) {
            return new Timetable(in);
        }

        public Timetable[] newArray(int size) {
            return new Timetable[size];
        }
    };
}
