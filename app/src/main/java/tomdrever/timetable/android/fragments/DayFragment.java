package tomdrever.timetable.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utils.ColourUtils;
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

        if (savedInstanceState != null) {
            day = savedInstanceState.getParcelable("day");
        }

        PeriodListAdapter periodsAdapter = new PeriodListAdapter(LayoutInflater.from(view.getContext()));
        periodsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        periodsRecyclerView.setAdapter(periodsAdapter);

        dayNameBar.setBackgroundColor(ColourUtils.lighten(day.getColour()));

        nameTextView.setText(day.getName());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("day", day);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }

    class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.PeriodViewHolder> {
        private final LayoutInflater inflater;

        public PeriodListAdapter(LayoutInflater inflater) { this.inflater = inflater; }

        @Override
        public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PeriodViewHolder(inflater.inflate(R.layout.view_card_view_period, parent, false));
        }

        @Override
        public void onBindViewHolder(PeriodViewHolder holder, int position) {
            // TODO - don't just use a list of periods for the list, since context mode needs to
            // display each hour(?) not just the periods
            holder.bind(day.getPeriods().get(position), position);
        }

        @Override
        public int getItemCount() { return day.getPeriods().size(); }

        // TODO - edit the period display so the "breaks" look good
        public class PeriodViewHolder extends RecyclerView.ViewHolder {
            Period period;

            @BindView(R.id.period_name_text) TextView nameTextView;
            @BindView(R.id.period_start_text) TextView startTimeTextView;
            @BindView(R.id.period_end_text) TextView endTimeTextView;
            @BindView(R.id.period_timeline_circle_indicator) ImageView periodTimelineCircleView;
            @BindView(R.id.period_timeline_line_indicator) View periodTimelineLineView;

            public PeriodViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PeriodBottomSheetDialogFragment.newPeriodBottomSheetDialogFragment(period)
                                .show(getActivity().getSupportFragmentManager(), "period_bottom_sheet");
                    }
                });
            }

            public void bind(Period item, int position) {
                this.period = item;

                nameTextView.setText(period.getName());

                periodTimelineCircleView.setImageResource(R.drawable.circle);
                periodTimelineCircleView.setColorFilter(period.getColour());

                periodTimelineLineView.setBackgroundColor(ColourUtils.lighten(day.getColour()));

                if (position == day.getPeriods().size() - 1) {
                    // TODO - half the width of the timeline line for the last item
                }

                startTimeTextView.setText(TimeUtils.formatTime(period.getStartTime().getHourOfDay(),
                        period.getStartTime().getMinuteOfHour()));

                endTimeTextView.setText(TimeUtils.formatTime(period.getEndTime().getHourOfDay(),
                        period.getEndTime().getMinuteOfHour()));
            }
        }
    }
}
