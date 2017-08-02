package tomdrever.timetable.android.controllers;

import tomdrever.timetable.data.Period;

public interface EditPeriodListener {
    void onFinishEditingPeriodClicked(Period period, int periodPosition);
    void onDeletePeriodClicked(int periodPosition);
}
