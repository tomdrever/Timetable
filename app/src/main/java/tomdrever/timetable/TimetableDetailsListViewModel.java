package tomdrever.timetable;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

public class TimetableDetailsListViewModel extends BaseObservable{
    private List<TimetableDetails> timetables;

    public TimetableDetailsListViewModel(List<TimetableDetails> timetables) {
        this.timetables = timetables;
    }

    @Bindable
    public List<TimetableDetails> getTimetables(){
        return timetables;
    }

    public void addTimetableDetails(TimetableDetails timetableDetails){
        timetables.add(timetableDetails);
        notifyPropertyChanged(tomdrever.timetable.BR.timetables);
    }

    public void removeTimetableDetails(TimetableDetails timetableDetails){
        timetables.remove(timetableDetails);
        notifyPropertyChanged(tomdrever.timetable.BR.timetables);
    }
}
