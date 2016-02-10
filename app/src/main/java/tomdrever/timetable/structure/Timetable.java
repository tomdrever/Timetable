package tomdrever.timetable.structure;

import java.util.ArrayList;

import tomdrever.timetable.structure.Day;

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
    public ArrayList<Day> getDays(){
        return days;
    }
}
