package tomdrever.timetable;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.TimetableDetailViewHolder> {

    public TimetableDetailsListViewModel timetableDetailsListViewModel;
    private Context context;

    public CustomRecyclerViewAdapter(TimetableDetailsListViewModel timetableDetailsListViewModel, Context context){
        this.timetableDetailsListViewModel = timetableDetailsListViewModel;
        this.context = context;
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);

        return new TimetableDetailViewHolder(v, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete
                int position = (Integer)v.getTag();
                remove(position);
            }
        });
    }

    @Override
    public void onBindViewHolder(TimetableDetailViewHolder holder, int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        holder.timetableDateCreated.setText("Created: " + df.format(timetableDetailsListViewModel.getTimetables().get(i).dateCreated));
        String desc = timetableDetailsListViewModel.getTimetables().get(i).description;
        holder.timetableDescription.setText(desc != "" ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableName.setText(timetableDetailsListViewModel.getTimetables().get(i).name);

        holder.deleteTimetableButton.setTag(i);

        //
    }

    public void add(TimetableDetails timetableDetails){
        timetableDetailsListViewModel.addTimetableDetails(timetableDetails);
        notifyItemInserted(timetableDetailsListViewModel.getTimetables().size() + 1);
    }

    public void remove(int position) {
        // delete from filesysytem
        File fileToDelete = new File(context.getFilesDir() + "/" + timetableDetailsListViewModel.getTimetables().get(position).name);
        fileToDelete.delete();


        // delete from recyclerview
        timetableDetailsListViewModel.removeTimetableDetails(timetableDetailsListViewModel.getTimetables().get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, timetableDetailsListViewModel.getTimetables().size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetableDetailsListViewModel.getTimetables().size();
    }

    public static class TimetableDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;

        private TextView timetableName;
        private TextView timetableDateCreated;
        private TextView timetableDescription;

        private ImageButton editTimetableButton;
        private ImageButton deleteTimetableButton;

        public TimetableDetailViewHolder(final View itemView, View.OnClickListener editTimetableButtonListener, View.OnClickListener deleteTimetableButtonListener) {
            super(itemView);

            itemView.setOnClickListener(this);

            cardView = (CardView) itemView.findViewById(R.id.cv);

            timetableName = (TextView) itemView.findViewById(R.id.timetable_name);
            timetableDateCreated = (TextView) itemView.findViewById(R.id.timetable_date_created);
            timetableDescription = (TextView) itemView.findViewById(R.id.timetable_description);

            editTimetableButton = (ImageButton) itemView.findViewById(R.id.edit_timetable_button);
            editTimetableButton.setOnClickListener(editTimetableButtonListener);

            deleteTimetableButton = (ImageButton) itemView.findViewById(R.id.delete_timetable_button);
            deleteTimetableButton.setOnClickListener(deleteTimetableButtonListener);
        }

        @Override
        public void onClick(View v) {
            // Open timetable
        }
    }
}