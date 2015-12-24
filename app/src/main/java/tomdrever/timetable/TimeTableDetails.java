package tomdrever.timetable;

import java.util.Date;


public class TimetableDetails {
    public String name;
    public String description;
    public Date dateCreated;
    public Date lastEdited;
    public Timetable timetable;

    public TimetableDetails(String name, String description, Date dateCreated, Timetable timetable){
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timetable = timetable;
    }
}
