package tomdrever.timetable.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.Date;

import tomdrever.timetable.BR;

public class TimetableContainer extends BaseObservable implements Serializable {

    private String name;
    private String description;
    private Date dateCreated;
    private Timetable timetable;

    public TimetableContainer(String name, String description, Date dateCreated, Timetable timetable) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timetable = timetable;
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

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
        notifyPropertyChanged(BR.timetable);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        notifyPropertyChanged(BR.dateCreated);
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
