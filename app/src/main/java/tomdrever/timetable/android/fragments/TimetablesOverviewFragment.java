package tomdrever.timetable.android.fragments;

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
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.android.TimetablesOverviewRecyclerViewAdapter;
import tomdrever.timetable.android.listeners.CardTouchedListener;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.FragmentTimetablesOverviewBinding;

import java.util.Collections;

public class TimetablesOverviewFragment extends Fragment implements CardTouchedListener {
    private ObservableArrayList<TimetableContainer> timetableContainers;
    private TimetableFileManager fileManager;

    private TimetableClickedListener cardClickedListener;
    private NewTimetableClickListener newTimetableClickListener;

    private TimetablesOverviewRecyclerViewAdapter recyclerViewAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTimetablesOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timetables_overview, container, false);

        recyclerViewAdapter = new TimetablesOverviewRecyclerViewAdapter(timetableContainers, this);
        binding.setTimetables(timetableContainers);

        fileManager = new TimetableFileManager(getContext());

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
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Store timetable data temporarily, so it can be restored
                final TimetableContainer tempTimetableContainer = timetableContainers.get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                remove(tempPosition);
                Snackbar.make(viewHolder.itemView, tempTimetableContainer.getName() + " deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restore
                        add(tempTimetableContainer, tempPosition);
                    }
                }).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(timetableContainers, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(timetableContainers, i, i - 1);
                    }
                }

                recyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
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
    public void onStop() {
        for (TimetableContainer timetableContainer : timetableContainers) {
            timetableContainer.setIndex(timetableContainers.indexOf(timetableContainer));
            fileManager.save(timetableContainer);
        }

        super.onStop();
    }

    private void add(TimetableContainer timetableContainer, int position) {
        fileManager.save(timetableContainer);
        timetableContainers.add(position, timetableContainer);
        recyclerViewAdapter.notifyItemInserted(position);
    }

    private void remove(int position) {
        fileManager.delete(timetableContainers.get(position).getName());
        timetableContainers.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static TimetablesOverviewFragment newInstance(ObservableArrayList<TimetableContainer> timetables,
                                                         TimetableClickedListener cardClickedListener,
                                                         NewTimetableClickListener newTimetableClickListener) {
        TimetablesOverviewFragment newFragment = new TimetablesOverviewFragment();
        newFragment.timetableContainers = timetables;
        newFragment.cardClickedListener = cardClickedListener;
        newFragment.newTimetableClickListener = newTimetableClickListener;
        return newFragment;
    }

    @Override
    public void onCardClicked(RecyclerView.ViewHolder viewHolder, int position) {
        cardClickedListener.onTimetableClicked(position);
    }

    @Override
    public void onCardDragHandleTouched(RecyclerView.ViewHolder viewHolder, int position) {
        itemTouchHelper.startDrag(viewHolder);
    }


    public interface TimetableClickedListener {
        void onTimetableClicked(int cardPosition);
    }

    public interface NewTimetableClickListener {
        void onNewTimetableClicked();
    }
}
