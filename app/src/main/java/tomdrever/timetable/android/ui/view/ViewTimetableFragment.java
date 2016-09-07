package tomdrever.timetable.android.ui.view;


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
import tomdrever.timetable.android.ui.FragmentBackPressedListener;
import tomdrever.timetable.android.ui.DaysRecyclerViewAdapter;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.FragmentViewTimetableBinding;

public class ViewTimetableFragment extends Fragment implements DaysRecyclerViewAdapter.DayCardClickListener {

    private TimetableContainer timetableContainer;

    private FragmentBackPressedListener fragmentBackPressedListener;
    private ViewEditPressedListener viewEditPressedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind timetable to layout
        FragmentViewTimetableBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_timetable, container, false);
        binding.setTimetable(timetableContainer);

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
                    //TODO - back w/ backstack? Set whether timetable was changed?
                    fragmentBackPressedListener.onFragmentBackPressed();
                }
            });
        }

        //endregion

        //region RecyclerView
        final RecyclerView daysListRecyclerView = (RecyclerView) getView().findViewById(R.id.view_timetable_days_list_recyclerview);

        final DaysRecyclerViewAdapter recyclerViewAdapter = new DaysRecyclerViewAdapter(
                timetableContainer.getTimetable().getDays(), this);

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
    public void onCardClicked(DaysRecyclerViewAdapter.DayViewHolder dayViewHolder, int position) {
        // TODO - switch to ViewDayFragment
    }

    public interface ViewEditPressedListener {
        void onViewEditPressed();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == TimetableActivityCodes.EDIT_NEW_TIMETABLE_SUCCESSFUL_CODE) {
                // Editing (or creation) has successfully finished
                // Unpack data and set to timetable object
                // FIXME - timetable must be rebound. Zut.
                timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetabledetails");
                binding.setTimetable(timetableContainer);
                hasTimetableChanged = true;
                Toast.makeText(this, "Saved changes", Toast.LENGTH_SHORT).show();
            } else if (resultCode == TimetableActivityCodes.EDIT_NEW_TIMETABLE_FAILED_CODE) {
                // Editing has finished, but changes have not been saved.

                // TODO - Do nothing?
                Toast.makeText(this, "Discarded changes", Toast.LENGTH_SHORT).show();
            } else if (resultCode == TimetableActivityCodes.CREATE_NEW_TIMETABLE_FAILED_CODE) {
                // Creation has finished, but changes have not been saved. Close
                // activity

                Toast.makeText(this, "Discarded timetable", Toast.LENGTH_SHORT).show();
                setResult(TimetableActivityCodes.VIEW_FINISHED_TIMETABLE_UNCHANGED);
                finish();
            }
        }
    }

    */
}
