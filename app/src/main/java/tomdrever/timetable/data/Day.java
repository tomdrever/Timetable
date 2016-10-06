package tomdrever.timetable.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Day extends BaseObservable implements Serializable {
    private ArrayList<Period> periods;

    @Bindable
    public ArrayList<Period> getPeriods() {
        return periods;
    }

    public void addPeriod(Period period) {
        // TODO -  reorganise periods on addition or removal to order chronologically
        periods.add(period);
    }

    public void removePeriod(Period period){
        periods.remove(period);
    }


    private String name;

    @Bindable
    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public Day(String name) {
        this.name = name;
        periods = new ArrayList<>();
    }

    public Day(Day day) {
        name = day.getName();
        periods = new ArrayList<>(day.getPeriods());
    }


    @Bindable
    public LocalTime getStartTime(){
        return periods.get(0).getStartTime();
    }

    @Bindable
    public LocalTime getEndTime(){
        return  periods.get(periods.size() - 1).getEndTime();
    }
}
