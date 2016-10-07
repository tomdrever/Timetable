package tomdrever.timetable.data;

public interface DataValueChangedListener {
    void onValueAdded(int position);
    void onValueRemoved(int position);
}
