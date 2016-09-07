package tomdrever.timetable.data;

public interface TimetableValueChangedListener {
    void onValueAdded(int position);
    void onValueRemoved(int position);
}
