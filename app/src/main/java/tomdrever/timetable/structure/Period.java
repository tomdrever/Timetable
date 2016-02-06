package tomdrever.timetable.structure;

import org.joda.time.Duration;
import org.joda.time.LocalTime;

public class Period {
    public Period(String name){
        periodMetaData = new PeriodMetaData(name);
    }
    private PeriodMetaData periodMetaData;
    public void setPeriodMetaData(PeriodMetaData periodMetaData){
        this.periodMetaData = periodMetaData;
    }
    public PeriodMetaData getPeriodMetaData(){
        return periodMetaData;
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
}
