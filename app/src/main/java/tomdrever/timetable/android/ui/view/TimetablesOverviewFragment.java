package tomdrever.timetable.android.ui.view;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import tomdrever.timetable.R;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityTimetablesOverviewBinding;

public class TimetablesOverviewFragment extends Fragment {
    private TimetablesOverviewRecyclerViewAdapter recyclerViewAdapter;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewAdapter = new TimetablesOverviewRecyclerViewAdapter(getContext());
        ActivityTimetablesOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_timetables_overview, container, false);
        binding.setTimetables(recyclerViewAdapter.timetables);

        if (rootView == null) {
            rootView = binding.getRoot();
        } else {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.timetables_overview_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Set up recyclerview (the main timetable display list)
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.timetables_list_recyclerview);

        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Store timetable temporarily, so it can be restored
                final TimetableContainer tempTimetableDetails = recyclerViewAdapter.timetables.get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                recyclerViewAdapter.remove(tempPosition);
                Snackbar.make(viewHolder.itemView, "Timetable deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restore
                        recyclerViewAdapter.add(tempTimetableDetails, tempPosition);
                    }
                }).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.new_timetable_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - switch fragment to ViewTimetable
            }
        });
                /*
                Intent intent = new Intent(v.getContext(), ViewTimetableActivity.class);
                intent.putExtra("isnewtimetable", true);

                // Send new empty timetable with name and description
                intent.putExtra("timetabledetails", new TimetableContainer(
                        "",
                        "",
                        Calendar.getInstance().getTime(), new Timetable()));
                startActivityForResult(intent, 100);
            }*/

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
