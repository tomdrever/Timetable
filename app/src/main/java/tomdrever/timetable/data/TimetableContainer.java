package tomdrever.timetable.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import tomdrever.timetable.BR;

import java.io.Serializable;
import java.util.Date;

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

    public TimetableContainer(TimetableContainer timetableContainer) {
        this.name = timetableContainer.getName();
        this.description = timetableContainer.getDescription();
        this.dateCreated = timetableContainer.getDateCreated();
        this.timetable = new Timetable(timetableContainer.getTimetable());
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

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
