package tomdrever.timetable.data;

import org.joda.time.LocalTime;

import java.util.ArrayList;

public class Day {
    private ArrayList<Period> periods;
    public void addPeriod(Period period){
        periods.add(period);
    }
    public void removePeriod(Period period){
        periods.remove(period);
    }
    public int getPeriodCount(){
        return periods.size();
    }

    private String name;
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public Day(String name) {
        setName(name);
        periods = new ArrayList<>();
    }

    // TODO -  reorganise periods on addition or removal to order chronologically
    public LocalTime getStartTime(){
        return periods.get(0).getStartTime();
    }

    public LocalTime getEndTime(){
        return  periods.get(periods.size() - 1).getEndTime();
    }
}
