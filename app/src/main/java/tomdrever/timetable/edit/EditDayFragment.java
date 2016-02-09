package tomdrever.timetable.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tomdrever.timetable.R;
import tomdrever.timetable.structure.Day;

public class EditDayFragment extends Fragment {

    private Day day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_day, container, false);

        ((TextView) rootView.findViewById(R.id.testtext)).setText("Day: " + day.getName());

        return rootView;
    }

    public static EditDayFragment newInstance(Day day) {
        EditDayFragment fragment = new EditDayFragment();
        fragment.day = day;
        return fragment;
    }
}