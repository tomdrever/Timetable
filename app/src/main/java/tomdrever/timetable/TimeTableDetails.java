package tomdrever.timetable;

import java.util.Date;


public class TimeTableDetails {
    public String name;
    public String description;
    public Date dateCreated;
    public Date lastEdited;
    public TimeTable timeTable;

    public TimeTableDetails(String name, String description, Date dateCreated, TimeTable timeTable){
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timeTable = timeTable;
    }
}
