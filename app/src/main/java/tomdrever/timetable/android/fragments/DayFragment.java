package tomdrever.timetable.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utils.ColorUtils;
import tomdrever.timetable.utils.TimeUtils;

public class DayFragment extends Fragment {
    private Unbinder unbinder;
    private Day day;

    @BindView(R.id.day_periods_recyclerview) RecyclerView periodsRecyclerView;
    @BindView(R.id.day_name_textview) TextView nameTextView;
    @BindView(R.id.day_name_bar) FrameLayout dayNameBar;

    public static DayFragment newDayFragment(Day day) {
        DayFragment fragment = new DayFragment();
        fragment.day = day;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_day, container, false);
        unbinder = ButterKnife.bind(this, view);

        PeriodListAdapter periodsAdapter = new PeriodListAdapter(LayoutInflater.from(view.getContext()));
        periodsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        periodsRecyclerView.setAdapter(periodsAdapter);

        // TODO - replace with day.getColour()
        dayNameBar.setBackgroundColor(ColorUtils.lighten(ContextCompat.getColor(getContext(), R.color.blue)));

        nameTextView.setText(day.getName());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }

    class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.PeriodViewHolder> {
        private final LayoutInflater inflater;

        public PeriodListAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PeriodViewHolder(inflater.inflate(R.layout.period_card, parent, false));
        }

        @Override
        public void onBindViewHolder(PeriodViewHolder holder, int position) {
            holder.bind(day.getPeriods().get(position));
        }

        @Override
        public int getItemCount() {
            return day.getPeriods().size();
        }

        public class PeriodViewHolder extends RecyclerView.ViewHolder {
            Period period;

            @BindView(R.id.period_name_text)
            TextView nameTextView;
            @BindView(R.id.period_start_text) TextView startTimeTextView;
            @BindView(R.id.period_end_text) TextView endTimeTextView;

            public PeriodViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            public void bind(Period item) {
                this.period = item;

                nameTextView.setText(period.getName());

                startTimeTextView.setText(TimeUtils.formatTime(period.getStartTime().getHourOfDay(),
                        period.getStartTime().getMinuteOfHour()));

                endTimeTextView.setText(TimeUtils.formatTime(period.getEndTime().getHourOfDay(),
                        period.getEndTime().getMinuteOfHour()));
            }
        }
    }
}
