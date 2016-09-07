package tomdrever.timetable.android.ui.edit;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import tomdrever.timetable.R;
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.android.ui.DaysRecyclerViewAdapter;
import tomdrever.timetable.android.ui.FragmentBackPressedListener;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.data.TimetableValueChangedListener;
import tomdrever.timetable.databinding.FragmentEditTimetableBinding;

import java.util.Collections;

public class EditTimetableFragment extends Fragment implements DaysRecyclerViewAdapter.DayCardClickListener,
        TimetableValueChangedListener {
    private TimetableContainer timetableContainer;
    private boolean isNewTimetable;

    private NewTimetableFinishedListener newTimetableFinishedListener;
    private FragmentBackPressedListener fragmentBackPressedListener;

    private DaysRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        timetableContainer.getTimetable().setValueChangedListener(this);

        // Bind timetable to layout
        FragmentEditTimetableBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_timetable, container, false);
        binding.setTimetableContainer(timetableContainer);
        binding.setIsnewtimetable(isNewTimetable);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Toolbar
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.edit_timetable_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Todo - onbackpressed, discard changes, etc
                    fragmentBackPressedListener.onFragmentBackPressed();
                }
            });
        }
        //endregion

        //region RecyclerView
        final RecyclerView daysListRecyclerView = (RecyclerView) getView().findViewById(R.id.edit_timetable_days_list_recyclerview);

        recyclerViewAdapter = new DaysRecyclerViewAdapter(
                timetableContainer.getTimetable().getDays(), this);

        daysListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        daysListRecyclerView.setAdapter(recyclerViewAdapter);
        //endregion

        //region FAB
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.new_day_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetableContainer.getTimetable().addDay(new Day(String.format("Today %s", timetableContainer.getTimetable().getDays().size() + 1)));

            }
        });
        //endregion

        //region ItemTouchHelper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean isLongPressDragEnabled() {
                return super.isLongPressDragEnabled();
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                final Day tempDay = recyclerViewAdapter.getDays().get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                timetableContainer.getTimetable().removeDay(tempPosition);
                Snackbar.make(viewHolder.itemView, "Day deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Re-add
                        timetableContainer.getTimetable().addDay(tempDay, tempPosition);
                    }
                }).show();

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                Collections.swap(recyclerViewAdapter.getDays(), viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }
        };
        //endregion

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(daysListRecyclerView);

        // Set name and description to text boxes
        if (!isNewTimetable) {
            ((EditText) getView().findViewById(R.id.edit_timetable_name)).setText(timetableContainer.getName());
            EditText descriptionText = ((EditText) getView().findViewById(R.id.edit_timetable_description));
            if (!descriptionText.getText().toString().equals("No description")) {
                descriptionText.setText(timetableContainer.getDescription());
            }
        }
    }

    public static EditTimetableFragment newInstance(TimetableContainer timetableContainer, boolean isNewTimetable,
                                                    NewTimetableFinishedListener newTimetableFinishedListener,
                                                    FragmentBackPressedListener fragmentBackPressedListener) {
        EditTimetableFragment newFragment = new EditTimetableFragment();

        newFragment.isNewTimetable = isNewTimetable;

        newFragment.timetableContainer = timetableContainer;

        newFragment.newTimetableFinishedListener = newTimetableFinishedListener;
        newFragment.fragmentBackPressedListener = fragmentBackPressedListener;

        return newFragment;
    }

    @Override
    public void onCardClicked(DaysRecyclerViewAdapter.DayViewHolder dayViewHolder, int position) {
        // TODO - launch edit day fragment
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_timetable, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish_editing_timetable: // On "done" pressed
                EditText editTimetableName = (EditText) getView().findViewById(R.id.edit_timetable_name);
                // Check the timetable has been given a name
                String name = editTimetableName.getText().toString().trim();
                if (name.isEmpty()) {
                    // If not, tell the user
                    Toast.makeText(getContext(), "The timetable needs a name!", Toast.LENGTH_SHORT).show();

                    // TODO - highlight name field to the user in some way - USER, DO THIS!
                } else {
                    // TODO - if (isnewtimetable) - check if the name is already used.
                    // If so, ask the user if they want to overwrite the tt or go back

                    TimetableFileManager fileManager = new TimetableFileManager(getContext());

                    // If it's not a new timetable, delete the old file (just in case)
                    if (!isNewTimetable) {
                        fileManager.delete(timetableContainer.getName());
                    }

                    // Set name and description from text
                    timetableContainer.setName(editTimetableName.getText().toString());

                    EditText editTimetableDescription = (EditText) getView().findViewById(R.id.edit_timetable_description);
                    String desc = editTimetableDescription.getText().toString().trim();
                    if (desc.isEmpty()) {
                        timetableContainer.setDescription("No description");
                    } else {
                        timetableContainer.setDescription(editTimetableDescription.getText().toString());
                    }

                    // Save over timetable
                    fileManager.save(timetableContainer);

                    newTimetableFinishedListener.OnNewTimetableFinished();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onValueAdded(int position) {
        recyclerViewAdapter.insertDayView(position);
    }

    @Override
    public void onValueRemoved(int position) {
        recyclerViewAdapter.removeDayView(position);
    }

    public interface NewTimetableFinishedListener {
        void OnNewTimetableFinished();
    }
}
