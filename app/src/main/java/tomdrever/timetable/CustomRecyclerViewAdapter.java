package tomdrever.timetable;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.TimetableDetailViewHolder>{

    private List<TimeTableDetails> timeTableDetailsList;
    private int lastPosition = -1;
    private Context context;

    public CustomRecyclerViewAdapter(List<TimeTableDetails> timeTableDetailsList, Context context){
        this.timeTableDetailsList = timeTableDetailsList;
        this.context = context;
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);
        return new TimetableDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TimetableDetailViewHolder holder, int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        holder.timetableDateCreated.setText("Created: " + df.format(timeTableDetailsList.get(i).dateCreated));
        String desc = timeTableDetailsList.get(i).description;
        holder.timetableDescription.setText(desc != "" ? desc : "No description");
        holder.timetableName.setText(timeTableDetailsList.get(i).name);

        // Animate?
        setAnimation(holder.cv, i);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
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
        return timeTableDetailsList.size();
    }

    public static class TimetableDetailViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView timetableName;
        TextView timetableDateCreated;
        TextView timetableDescription;

        TimetableDetailViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            timetableName = (TextView)itemView.findViewById(R.id.timetable_name);
            timetableDateCreated = (TextView)itemView.findViewById(R.id.timetable_date_created);
            timetableDescription = (TextView)itemView.findViewById(R.id.timetable_description);
        }
    }

}