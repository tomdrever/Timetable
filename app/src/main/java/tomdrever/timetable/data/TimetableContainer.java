package tomdrever.timetable.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import tomdrever.timetable.BR;

import java.io.Serializable;
import java.util.Date;

public class TimetableContainer extends BaseObservable implements Serializable, Comparable<TimetableContainer> {

    private String name;
    private String description;
    private Date dateCreated;
    private Timetable timetable;
    private int index;

    public TimetableContainer(String name, String description, Date dateCreated, Timetable timetable, int index) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timetable = timetable;
        this.index = index;
    }

    public TimetableContainer(TimetableContainer timetableContainer) {
        this.name = timetableContainer.getName();
        this.description = timetableContainer.getDescription();
        this.dateCreated = timetableContainer.getDateCreated();
        this.timetable = new Timetable(timetableContainer.getTimetable());
        this.index = timetableContainer.getIndex();
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public Date getDateCreated() {
        return dateCreated;
    }

    @Bindable
    public Timetable getTimetable() {
        return timetable;
    }

    private int getIndex() {
        return index;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
        notifyPropertyChanged(BR.timetable);
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setIndex(int i) {
        index = i;
    }

    @Override
    public int compareTo(@NonNull TimetableContainer timetableContainer) {
        return Integer.valueOf(this.index).compareTo(timetableContainer.index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimetableContainer) {
            TimetableContainer other = (TimetableContainer) obj;
            return other.getName().equals(name) && other.getDescription().equals(description) &&
                    other.getDateCreated() == dateCreated && other.getTimetable().equals(timetable);
        } else {
            return super.equals(obj);
        }
    }
}
