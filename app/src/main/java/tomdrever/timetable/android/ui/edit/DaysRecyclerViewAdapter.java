package tomdrever.timetable.android.ui.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.view.ViewTimetableActivity;
import tomdrever.timetable.data.Day;

public class DaysRecyclerViewAdapter extends RecyclerView.Adapter<DaysRecyclerViewAdapter.DayViewHolder> {
    public final ArrayList<Day> days;
    private final Context context;

    public DaysRecyclerViewAdapter( ArrayList<Day> days, Context context) {
        this.days = days;
        this.context = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);

        return new DayViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Editing an existing day
                Intent intent = new Intent(context, EditDayActivity.class);
                intent.putExtra("dayjson", new Gson().toJson(days.get((int)v.getTag())));
                ((Activity)context).startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int i) {
        holder.dayNameView.setText(days.get(i).getName());

        holder.itemView.setTag(i);
    }

    public void add(Day day) {
        add(day, days.size());
    }

    public void add(Day day, int position) {
        days.add(position, day);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, days.size());
    }

    public void remove(int position) {
        days.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, days.size());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder{
        private TextView dayNameView;
        private View itemView;

        public DayViewHolder(final View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(onClickListener);

            dayNameView = (TextView)itemView.findViewById(R.id.day_name_card_title);
        }
    }
}