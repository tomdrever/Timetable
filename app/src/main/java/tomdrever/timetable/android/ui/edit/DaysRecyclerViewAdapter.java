package tomdrever.timetable.android.ui.edit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Day;

import java.util.ArrayList;

class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DayViewHolder> {
    final ArrayList<Day> days;

    private final DayCardClickListener listener;

    DaysRecyclerViewAdapter( ArrayList<Day> days, Context context, DayCardClickListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);

        return new DayViewHolder(view, listener);
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

    public void add(Day day) {
        add(day, days.size());
    }

    public void add(Day day, int position) {
        days.add(position, day);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, days.size());
    }

    void remove(int position) {
        days.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, days.size());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder{
        private TextView dayNameView;
        private View itemView;

        DayViewHolder(final View itemView, final DayCardClickListener listener) {
            super(itemView);
            this.itemView = itemView;

            dayNameView = (TextView)itemView.findViewById(R.id.day_name_card_title);
        }
    }

    interface DayCardClickListener {
        void onCardClicked(DayViewHolder dayViewHolder, int position);
    }
}