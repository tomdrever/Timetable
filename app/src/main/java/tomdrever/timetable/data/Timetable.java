package tomdrever.timetable.data;

import java.util.ArrayList;

public class Timetable implements Comparable<Timetable>{
    private String name;
    private String description;
    private ArrayList<Day> days;
    private int index;

    public Timetable() {
        days = new ArrayList<>();
    }

    // Clone constructor
    public Timetable(Timetable timetable) {
	    days = new ArrayList<>();
	    for (int i = 0; i < timetable.getDays().size(); i++) {
		    days.add(new Day(timetable.getDays().get(i)));
	    }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDay(Day day){
        addDay(day, days.size());
    }

    public void addDay(Day day, int position) {
        days.add(position, day);
    }

    public void removeDay(int position) {
        days.remove(position);
    }

    public ArrayList<Day> getDays(){
        return days;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Timetable timetable) {
        return Integer.compare(index, timetable.index);
    }
}
