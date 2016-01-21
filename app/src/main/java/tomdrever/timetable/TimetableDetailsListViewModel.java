package tomdrever.timetable;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

public class TimetableDetailsListViewModel extends BaseObservable{
    private List<TimetableDetails> timetables;

    @Bindable
    public String sizeString;

    public TimetableDetailsListViewModel(List<TimetableDetails> timetables) {
        this.timetables = timetables;
        this.sizeString = "none";
    }

    @Bindable
    public List<TimetableDetails> getTimetables(){
        return timetables;
    }

    public void addTimetableDetails(TimetableDetails timetableDetails){
        sizeString = Integer.toString(timetables.size());
        timetables.add(timetableDetails);
        notifyPropertyChanged(tomdrever.timetable.BR.timetables);
    }

    public void removeTimetableDetails(TimetableDetails timetableDetails){
        sizeString = Integer.toString(timetables.size());
        timetables.remove(timetableDetails);
        notifyPropertyChanged(tomdrever.timetable.BR.timetables);
    }
}
