package tomdrever.timetable.android.listeners;

import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.TimetableContainer;

public interface EditingFinishedListener {
    void onEditingTimetableFinished(TimetableContainer timetableContainer);
	void onEditingDayFinished(Day day, int position);
}
