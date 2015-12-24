package tomdrever.timetable;

import java.util.Date;

public class Period {
    private PeriodData periodData;
    public void setPeriodData(PeriodData periodData){
        this.periodData = periodData;
    }
    public PeriodData getPeriodData(){
        return periodData;
    }

    private Date startTime;
    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }
    public Date getStartTime(){
        return startTime;
    }

    private Date duration;
    public void setDuration(Date duration){
        this.duration = duration;
    }
    public Date getDuration(){
        return duration;
    }
}
