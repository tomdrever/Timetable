package tomdrever.timetable;


import java.util.ArrayList;

public class Timetable {
    private ArrayList<Day> days;

    public void addDay(Day day){
        days.add(day);
    }
    public void removeDay(Day day){
        days.remove(day);
    }

    public Timetable(){
        days = new ArrayList<>();
    }
}
