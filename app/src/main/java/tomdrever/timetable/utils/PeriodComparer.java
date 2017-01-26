package tomdrever.timetable.utils;

import java.util.Comparator;

import tomdrever.timetable.data.Period;

public class PeriodComparer implements Comparator<Period> {
    @Override
    public int compare(Period period, Period other) {

        return period.getStartTime().isBefore(other.getStartTime()) ? -1 : +1;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}
