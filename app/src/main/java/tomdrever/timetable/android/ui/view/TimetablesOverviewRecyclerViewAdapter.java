package tomdrever.timetable.android.ui.view;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import tomdrever.timetable.R;
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.data.TimetableContainer;

public class TimetablesOverviewRecyclerViewAdapter extends RecyclerView.Adapter<TimetablesOverviewRecyclerViewAdapter.TimetableDetailViewHolder> {
    public ObservableArrayList<TimetableContainer> timetables;
    private final Context context;
    private final TimetableFileManager fileManager;

    public TimetablesOverviewRecyclerViewAdapter(Context context){
        this.timetables = new ObservableArrayList<>();
        this.context = context;
        this.fileManager = new TimetableFileManager(context);

        loadTimetables();
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);

        return new TimetableDetailViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ViewTimetableActivity.class);
                intent.putExtra("timetabledetails", timetables.get((int)v.getTag()));

                ((Activity)context).startActivityForResult(intent, 100);*/

                // TODO - start ViewTimetable
            }
        });
    }

    @Override
    public void onBindViewHolder(TimetableDetailViewHolder holder, int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        holder.timetableDateCreatedView.setText("Created: " + df.format(timetables.get(i).getDateCreated()));
        String desc = timetables.get(i).getDescription();
        holder.timetableDescriptionView.setText(desc != "" ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableNameView.setText(timetables.get(i).getName());

        holder.itemView.setTag(i);
    }

    public void add(TimetableContainer timetableContainer, int position){
        // add to filesystem
        fileManager.save(timetableContainer);

        // add to recyclerview
        timetables.add(position, timetableContainer);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, timetables.size());
        notifyDataSetChanged();
    }

    public void remove(int position) {
        // delete from filesysytem
        fileManager.delete(timetables.get(position).getName());

        // delete from recyclerview
        timetables.remove(timetables.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, timetables.size());
        notifyDataSetChanged();
    }

    public void loadTimetables() {
        timetables.clear();
        timetables.addAll(fileManager.loadAll());

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetables.size();
    }

    public static class TimetableDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView timetableNameView;
        private TextView timetableDateCreatedView;
        private TextView timetableDescriptionView;
        private View itemView;

        public TimetableDetailViewHolder(final View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(onClickListener);

            timetableNameView = (TextView) itemView.findViewById(R.id.timetable_name);
            timetableDateCreatedView = (TextView) itemView.findViewById(R.id.timetable_date_created);
            timetableDescriptionView = (TextView) itemView.findViewById(R.id.timetable_description);
        }
    }
}