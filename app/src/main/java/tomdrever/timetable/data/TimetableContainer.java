package tomdrever.timetable.data;

import java.io.Serializable;
import java.util.Date;

public class TimetableContainer implements Serializable{
    public TimetableContainer(String name, String description, Date dateCreated, Timetable timetable) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timetable = timetable;
    }

    public String name;
    public String description;
    public Date dateCreated;
    public Timetable timetable;
}
