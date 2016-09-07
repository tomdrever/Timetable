package tomdrever.timetable.android.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Day;

import java.util.ArrayList;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DayViewHolder> {
    private ArrayList<Day> days;

    public ArrayList<Day> getDays() {
        return days;
    }

    private final DayCardClickListener listener;

    public DaysRecyclerViewAdapter(ArrayList<Day> days, DayCardClickListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);

        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int i) {
        holder.dayNameView.setText(days.get(i).getName());

        holder.itemView.setTag(i);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardClicked(holder, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder{
        private TextView dayNameView;
        private View itemView;

        DayViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;

            dayNameView = (TextView)itemView.findViewById(R.id.day_name_card_title);
        }
    }

    public interface DayCardClickListener {
        void onCardClicked(DayViewHolder dayViewHolder, int position);
    }
}