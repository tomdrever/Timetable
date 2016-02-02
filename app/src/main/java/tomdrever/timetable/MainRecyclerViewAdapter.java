package tomdrever.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.TimetableDetailViewHolder> {

    public ArrayList<TimetableContainer> timetableDetails;
    private Context context;

    public MainRecyclerViewAdapter(ArrayList<TimetableContainer> timetableDetails, Context context){
        this.timetableDetails = timetableDetails;
        this.context = context;
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);

        return new TimetableDetailViewHolder(v, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch edittimetable
                Intent intent = new Intent(context, EditTimetableActivity.class);
                intent.putExtra("isnewtimetable", false);
                intent.putExtra("timetabledetailsjson", new Gson().toJson(timetableDetails.get((Integer)v.getTag())));
                ((Activity) context).startActivityForResult(intent, 200);
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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        holder.timetableDateCreated.setText("Created: " + df.format(timetableDetails.get(i).dateCreated));
        String desc = timetableDetails.get(i).description;
        holder.timetableDescription.setText(desc != "" ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableName.setText(timetableDetails.get(i).name);

        holder.deleteTimetableButton.setTag(i);
        holder.editTimetableButton.setTag(i);
    }

    public void add(TimetableContainer newTimetableContainer){
        timetableDetails.add(newTimetableContainer);
        notifyItemInserted(timetableDetails.size() + 1);
    }

    public void remove(int position) {
        // delete from filesysytem
        File fileToDelete = new File(context.getFilesDir() + "/" + timetableDetails.get(position).name);
        fileToDelete.delete();


        // delete from recyclerview
        timetableDetails.remove(timetableDetails.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, timetableDetails.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetableDetails.size();
    }

    public static class TimetableDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView timetableName;
        private TextView timetableDateCreated;
        private TextView timetableDescription;

        private ImageButton editTimetableButton;
        private ImageButton deleteTimetableButton;

        public TimetableDetailViewHolder(final View itemView, View.OnClickListener editTimetableButtonListener, View.OnClickListener deleteTimetableButtonListener) {
            super(itemView);

            itemView.setOnClickListener(this);

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