package tomdrever.timetable.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Timetable implements Serializable {
    public Timetable(){
        days = new ArrayList<>();
    }

    private ArrayList<Day> days;
    public void addDay(Day day){
        days.add(day);
    }
    public void addDay(Day day, int position) {
        days.add(position, day);
    }
    public void removeDay(Day day){
        days.remove(day);
    }
    public void removeDay(int position) {
        days.remove(position);
    }
    public ArrayList<Day> getDays(){
        return days;
    }
}
