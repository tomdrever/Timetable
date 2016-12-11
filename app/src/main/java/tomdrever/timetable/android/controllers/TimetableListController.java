package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.utility.TimetableFileManager;

public class TimetableListController extends BaseController {

    private TimetableFileManager fileManager;

    private ArrayList<Timetable> timetables;

    private TimetableListAdapter adapter;

    @BindView(R.id.timetables_list_recyclerview) RecyclerView recyclerView;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_timetable_list, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        if (fileManager == null) {
            fileManager = new TimetableFileManager(getApplicationContext());
        }

        timetables = fileManager.loadAll();

        adapter = new TimetableListAdapter(LayoutInflater.from(view.getContext()), timetables);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                final Timetable tempTimetableContainer = timetables.get(viewHolder.getAdapterPosition());
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
                        Timetable temp = timetables.get(i);
                        timetables.set(i, timetables.get(i + 1));
                        timetables.set(i + 1, temp);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Timetable temp = timetables.get(i);
                        timetables.set(i, timetables.get(i - 1));
                        timetables.set(i - 1, temp);
                    }
                }

                adapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void add(Timetable timetable, int position) {
        fileManager.save(timetable);
        timetables.add(position, timetable);
        adapter.notifyItemInserted(position);
    }

    private void remove(int position) {
        fileManager.delete(timetables.get(position).getName());
        timetables.remove(position);
        adapter.notifyItemRemoved(position);
    }

    class TimetableListAdapter extends RecyclerView.Adapter<TimetableListAdapter.TimetableViewHolder> {

        private final LayoutInflater inflater;
        private final ArrayList<Timetable> items;

        public TimetableListAdapter(LayoutInflater inflater, ArrayList<Timetable> timetables) {
            this.inflater = inflater;
            this.items = timetables;
        }

        @Override
        public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TimetableViewHolder(inflater.inflate(R.layout.timetable_card, parent, false));
        }

        @Override
        public void onBindViewHolder(TimetableViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.toArray().length;
        }

        public class TimetableViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.timetable_card_name) TextView nameTextView;
            @BindView(R.id.timetable_card_description) TextView descriptionTextView;

            public TimetableViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            public void bind(Timetable item) {
                nameTextView.setText(item.getName());
                descriptionTextView.setText(item.getDescription());
            }
        }
    }
}
