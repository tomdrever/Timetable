package tomdrever.timetable.android.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tomdrever.timetable.R;
import tomdrever.timetable.android.listeners.CardTouchedListener;
import tomdrever.timetable.android.DaysRecyclerViewAdapter;
import tomdrever.timetable.android.listeners.FragmentBackPressedListener;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.FragmentViewTimetableBinding;

public class ViewTimetableFragment extends Fragment implements CardTouchedListener {

    private TimetableContainer timetableContainer;

    private FragmentBackPressedListener fragmentBackPressedListener;
    private ViewEditPressedListener viewEditPressedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind timetable to layout
        FragmentViewTimetableBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_timetable, container, false);
        binding.setTimetableContainer(timetableContainer);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Toolbar
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.view_timetable_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentBackPressedListener.onFragmentBackPressed();
                }
            });
        }

        //endregion

        //region RecyclerView
        final RecyclerView daysListRecyclerView = (RecyclerView) getView().findViewById(R.id.view_timetable_days_list_recyclerview);

        final DaysRecyclerViewAdapter recyclerViewAdapter = new DaysRecyclerViewAdapter(
                timetableContainer.getTimetable().getDays(), false, this);

        daysListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        daysListRecyclerView.setAdapter(recyclerViewAdapter);
        //endregion

        // region FAB
        FloatingActionButton editTimetableFAB = (FloatingActionButton) getView().findViewById(R.id.edit_timetable_fab);

        if (editTimetableFAB != null) {
            editTimetableFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewEditPressedListener.onViewEditPressed();
                }
            });
        }
        //endregion
    }

    public static ViewTimetableFragment newInstance(TimetableContainer timetableContainer,
                                                    FragmentBackPressedListener fragmentBackPressedListener,
                                                    ViewEditPressedListener viewEditPressedListener) {
        ViewTimetableFragment newFragment = new ViewTimetableFragment();
        newFragment.timetableContainer = timetableContainer;
        newFragment.fragmentBackPressedListener = fragmentBackPressedListener;
        newFragment.viewEditPressedListener = viewEditPressedListener;
        return newFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCardClicked(RecyclerView.ViewHolder viewHolder, int position) {
		// TODO - launch ViewDay
    }

    @Override
    public void onCardDragHandleTouched(RecyclerView.ViewHolder viewHolder, int position) {
		// REM - nothing
    }

    public interface ViewEditPressedListener {
        void onViewEditPressed();
    }
}
