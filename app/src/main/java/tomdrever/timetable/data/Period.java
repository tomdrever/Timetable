package tomdrever.timetable.data;

import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Period implements Serializable {
    public Period(String name) {
        this.name = name;
	    this.periodDescription = new HashMap<>();
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

    public Duration getDuration() {
        return new Duration(startTime.toDateTimeToday(), endTime.toDateTimeToday());
    }

	private Map<String, Boolean> periodDescription;
	public void addDescriptionEntry(String description, Boolean isDisplayedDescription) {
		periodDescription.put(description, isDisplayedDescription);
	}
	public void removeDescriptionEntry(String description){
		periodDescription.remove(description);
	}
}
