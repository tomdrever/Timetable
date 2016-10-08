package tomdrever.timetable.data.listeners;

public interface DataValueChangedListener {
    void onValueAdded(int position);
    void onValueRemoved(int position);
}
