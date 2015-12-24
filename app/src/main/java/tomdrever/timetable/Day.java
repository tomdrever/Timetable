package tomdrever.timetable;

import java.util.ArrayList;

public class Day {
    private ArrayList<Period> periods;
    public void addPeriod(Period period){
        periods.add(period);
    }
    public void removePeriod(Period period){
        periods.remove(period);
    }

    private String name;
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public Day(String name) {
        setName(name);
    }
}
