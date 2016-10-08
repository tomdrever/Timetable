package tomdrever.timetable.android.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import tomdrever.timetable.android.PeriodsRecyclerViewAdapter;
import tomdrever.timetable.android.listeners.CardTouchedListener;
import tomdrever.timetable.android.listeners.EditingFinishedListener;
import tomdrever.timetable.android.listeners.FragmentBackPressedListener;
import tomdrever.timetable.data.DataValueChangedListener;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.databinding.FragmentEditDayBinding;

public class EditDayFragment extends Fragment implements CardTouchedListener, DataValueChangedListener,
		EditPeriodDialogFragment.FragmentSuccessListener{
    private Day day;
	private int dayPosition;

    private FragmentBackPressedListener fragmentBackPressedListener;
    private EditingFinishedListener editingFinishedListener;

	private PeriodsRecyclerViewAdapter recyclerViewAdapter;
	private ItemTouchHelper itemTouchHelper;

    // REM - periods cannot be reordered, they are automatically arranged chronologically
    // REM - clicking one (or clicking to add a new one) launches a dialog
    // REM - clicking finish is the same as going back, but the day is saved (woot)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditDayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_day, container, false);
        binding.setDay(day);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Toolbar
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.edit_day_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBackPressedListener.onFragmentBackPressed();
            }
        });
        //endregion

        //region RecyclerView
	    RecyclerView daysListRecyclerView = (RecyclerView) getView().findViewById(R.id.edit_day_periods_list_recyclerview);

	    recyclerViewAdapter = new PeriodsRecyclerViewAdapter(day.getPeriods(), this);

	    daysListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	    daysListRecyclerView.setAdapter(recyclerViewAdapter);
        //endregion

	    //region ItemTouchHelper
	    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
			    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

		    @Override
		    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
			    return false;
		    }

		    @Override
		    public boolean isItemViewSwipeEnabled() {
			    return true;
		    }

		    @Override
		    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
			    final Period tempPeriod = day.getPeriods().get(viewHolder.getAdapterPosition());
			    final int tempPosition = viewHolder.getAdapterPosition();
			    day.removePeriod(tempPosition);
			    Snackbar.make(viewHolder.itemView, tempPeriod.getName() + " deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
					    // Re-add
					    day.addPeriod(tempPeriod, tempPosition);
				    }
			    }).show();

		    }
	    };
	    //endregion

	    itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
	    itemTouchHelper.attachToRecyclerView(daysListRecyclerView);

        //region FAB
        FloatingActionButton fab = (FloatingActionButton)getView().findViewById(R.id.new_period_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - launch period dialog
	            EditPeriodDialogFragment dialogFragment = EditPeriodDialogFragment.newInstance(null, day.getPeriods().size(),
			            EditDayFragment.this);
	            dialogFragment.show(getFragmentManager(), "");
            }
        });
        //endregion

        // Bind name to nameEditText
        ((EditText) getView().findViewById(R.id.edit_day_name)).setText(day.getName());
    }

    public static EditDayFragment newInstance(Day day, int dayPosition,
                                              FragmentBackPressedListener fragmentBackPressedListener,
                                              EditingFinishedListener editingFinishedListener) {
        EditDayFragment newFragment = new EditDayFragment();
        newFragment.day = day;
	    newFragment.day.setValueChangedListener(newFragment);
	    newFragment.dayPosition = dayPosition;
        newFragment.fragmentBackPressedListener = fragmentBackPressedListener;
        newFragment.editingFinishedListener = editingFinishedListener;
        return newFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish_editing:
                // TODO - save day into timetable
	            EditText editDayName = (EditText) getView().findViewById(R.id.edit_day_name);
	            // Check the timetable has been given a name
	            String name = editDayName.getText().toString().trim();
	            if (name.isEmpty()) {
		            // If not, tell the user
		            Toast.makeText(getContext(), "The day needs a name!", Toast.LENGTH_SHORT).show();

		            // TODO - highlight name field to the user in some way - USER, DO THIS!
	            } else {
		            day.setName(name);
		            editingFinishedListener.onEditingDayFinished(day, dayPosition);
	            }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCardClicked(RecyclerView.ViewHolder viewHolder, int position) {
	    EditPeriodDialogFragment dialogFragment = EditPeriodDialogFragment.newInstance(day.getPeriods().get(position),
			    day.getPeriods().size(), EditDayFragment.this);
	    dialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void onCardDragHandleTouched(RecyclerView.ViewHolder viewHolder, int position) {
        // TODO - nothing
    }

	@Override
	public void onValueAdded(int position) {
		recyclerViewAdapter.notifyItemInserted(position);
	}

	@Override
	public void onValueRemoved(int position) {
		recyclerViewAdapter.notifyItemRemoved(position);
	}

	@Override
	public void onFragmentSuccess(Period period, int periodPosition) {
		if (periodPosition >= day.getPeriods().size()) {
			day.addPeriod(period);
		} else {
			day.getPeriods().set(periodPosition, period);
		}
	}
}
