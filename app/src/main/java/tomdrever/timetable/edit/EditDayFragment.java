package tomdrever.timetable.edit;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tomdrever.timetable.R;
import tomdrever.timetable.databinding.FragmentEditDayBinding;
import tomdrever.timetable.structure.Day;

public class EditDayFragment extends Fragment {

    private Day day;

    private FragmentEditDayBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind day to layout
        binding = FragmentEditDayBinding.inflate(getLayoutInflater(savedInstanceState));
        binding.setDay(day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return binding.getRoot();
    }

    public static EditDayFragment newInstance(Day day) {
        EditDayFragment fragment = new EditDayFragment();
        fragment.day = day;
        return fragment;
    }
}