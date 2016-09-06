package tomdrever.timetable.android.ui.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tomdrever.timetable.R;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.FragmentViewTimetableBinding;

public class ViewTimetableFragment extends Fragment {

    private TimetableContainer timetableContainer; // TODO - make observable

    private ViewBackPressedListener viewBackPressedListener;
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
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO - back w/ backstack? Set whether timetable was changed?
                    viewBackPressedListener.onViewBackPressed();
                }
            });
        }

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
                                                    ViewBackPressedListener viewBackPressedListener,
                                                    ViewEditPressedListener viewEditPressedListener) {
        ViewTimetableFragment newFragment = new ViewTimetableFragment();
        newFragment.timetableContainer = timetableContainer;
        newFragment.viewBackPressedListener = viewBackPressedListener;
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

    public interface ViewEditPressedListener {
        void onViewEditPressed();
    }

    public interface ViewBackPressedListener {
        void onViewBackPressed();
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
