package tomdrever.timetable.android.ui.edit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.FragmentBackPressedListener;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.databinding.FragmentEditDayBinding;

public class EditDayFragment extends Fragment {

    private Day day;

    private FragmentBackPressedListener fragmentBackPressedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditDayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_day, container, false);
        binding.setDay(day);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.edit_day_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBackPressedListener.onFragmentBackPressed();
            }
        });
    }

    public static EditDayFragment newInstance(Day day, FragmentBackPressedListener fragmentBackPressedListener) {
        EditDayFragment newFragment = new EditDayFragment();
        newFragment.day = day;
        newFragment.fragmentBackPressedListener = fragmentBackPressedListener;
        return newFragment;
    }
}
