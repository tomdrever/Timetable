package tomdrever.timetable;

import java.util.ArrayList;

public class Timetable {
    public Timetable(){
        days = new ArrayList<>();
    }

    private ArrayList<Day> days;
    public void addDay(Day day){
        days.add(day);
    }
    public void removeDay(Day day){
        days.remove(day);
    }
}
