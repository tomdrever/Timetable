package tomdrever.timetable.android.ui.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.*;
import tomdrever.timetable.R;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.FragmentTimetablesOverviewBinding;

public class TimetablesOverviewFragment extends Fragment implements TimetablesOverviewRecyclerViewAdapter.TimetableCardClickListener {
    private TimetablesOverviewRecyclerViewAdapter recyclerViewAdapter;

    private CardClickedListener cardClickedListener;
    private NewTimetableClickListener newTimetableClickListener;

    private ObservableArrayList<TimetableContainer> timetables;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTimetablesOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timetables_overview, container, false);

        recyclerViewAdapter = new TimetablesOverviewRecyclerViewAdapter(timetables, getContext(), this);
        binding.setTimetables(timetables);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Toolbar
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.timetables_overview_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //endregion

        //region RecyclerView
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.timetables_list_recyclerview);

        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //endregion

        //region ItemTouchHelper - swiping and dragging
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Store timetable data temporarily, so it can be restored
                final TimetableContainer tempTimetableContainer = recyclerViewAdapter.timetables.get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                recyclerViewAdapter.remove(tempPosition);
                Snackbar.make(viewHolder.itemView, "Timetable deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restore
                        recyclerViewAdapter.add(tempTimetableContainer, tempPosition);
                    }
                }).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // TODO - handle dragging (re-ordering) here?
                return false;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //endregion

        // region FAB
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.new_timetable_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTimetableClickListener.onNewTimetableClicked();
            }
        });
        //endregion
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static TimetablesOverviewFragment newInstance(ObservableArrayList<TimetableContainer> timetables,
                                                         CardClickedListener cardClickedListener,
                                                         NewTimetableClickListener newTimetableClickListener) {
        TimetablesOverviewFragment newFragment = new TimetablesOverviewFragment();
        newFragment.timetables = timetables;
        newFragment.cardClickedListener = cardClickedListener;
        newFragment.newTimetableClickListener = newTimetableClickListener;
        return newFragment;
    }

    @Override
    public void onCardClicked(TimetablesOverviewRecyclerViewAdapter.TimetableDetailViewHolder holder, int position) {
        cardClickedListener.onCardClicked(position);
    }

    public interface CardClickedListener {
        void onCardClicked(int cardPosition);
    }

    public interface NewTimetableClickListener {
        void onNewTimetableClicked();
    }
}
