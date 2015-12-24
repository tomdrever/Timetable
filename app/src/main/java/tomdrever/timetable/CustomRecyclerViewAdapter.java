package tomdrever.timetable;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.TimetableDetailViewHolder> {

    private List<TimetableDetails> timetableDetailsList;
    private int lastPosition = -1;
    private Context context;

    public CustomRecyclerViewAdapter(List<TimetableDetails> timetableDetailsList, Context context){
        this.timetableDetailsList = timetableDetailsList;
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

                timetableDetailsList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindViewHolder(TimetableDetailViewHolder holder, int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        holder.timetableDateCreated.setText("Created: " + df.format(timetableDetailsList.get(i).dateCreated));
        String desc = timetableDetailsList.get(i).description;
        holder.timetableDescription.setText(desc != "" ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableName.setText(timetableDetailsList.get(i).name);

        // Animate
        setFadeAnimation(holder.cardView, i, 0.2f, 1.0f);

        holder.deleteTimetableButton.setTag(i);
    }

    private void setFadeAnimation(View viewToAnimate, int position, float startAlpha, float endAlpha) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = new AlphaAnimation(0.2f, 1.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetableDetailsList.size();
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

            cardView = (CardView)itemView.findViewById(R.id.cv);

            timetableName = (TextView)itemView.findViewById(R.id.timetable_name);
            timetableDateCreated = (TextView)itemView.findViewById(R.id.timetable_date_created);
            timetableDescription = (TextView)itemView.findViewById(R.id.timetable_description);

            editTimetableButton = (ImageButton)itemView.findViewById(R.id.edit_timetable_button);
            editTimetableButton.setOnClickListener(editTimetableButtonListener);

            deleteTimetableButton = (ImageButton)itemView.findViewById(R.id.delete_timetable_button);
            deleteTimetableButton.setOnClickListener(deleteTimetableButtonListener);
        }

        @Override
        public void onClick(View v) {
            // Open timetable
        }
    }
}