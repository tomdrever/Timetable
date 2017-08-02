package tomdrever.timetable.android.controllers;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.android.fragments.ColourPickerDialogFragment;
import tomdrever.timetable.android.fragments.TimePickerDialogFragment;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utils.ColourUtils;
import tomdrever.timetable.utils.FragmentTags;
import tomdrever.timetable.utils.TimeUtils;

public class EditPeriodController extends BaseController implements ColourPickerDialogFragment.OnColourSetListener {

    private Period period;

    private int periodPosition;

    private EditPeriodListener editingFinishedListener;

    @BindView(R.id.period_name_input_layout) TextInputLayout periodNameInputLayout;

    @BindView(R.id.edit_period_name) EditText nameEditText;
    @BindView(R.id.edit_period_start_time_main_view) TextView startTimeMainTextView;
    @BindView(R.id.edit_period_start_time_second_view) TextView startTimeSecondTextView;
    @BindView(R.id.edit_period_end_time_main_view) TextView endTimeMainTextView;
    @BindView(R.id.edit_period_end_time_second_view) TextView endTimeSecondTextView;

    @BindView(R.id.colour_rect_image) ImageView periodColourImage;

    @BindView(R.id.delete_period_fab) FloatingActionButton deletePeriodButton;

    public EditPeriodController() {}

    public EditPeriodController(Period period, int periodPosition,
                                EditPeriodListener editingFinishedListener) {
        this.period = period;
        this.periodPosition = periodPosition;
        this.editingFinishedListener = editingFinishedListener;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_period, container, false);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        ColourPickerDialogFragment fragment =
                (ColourPickerDialogFragment) getAppCombatActivity().getSupportFragmentManager().findFragmentByTag(FragmentTags.PERIOD_COLOUR_PICKER_DIALOG);

        if (fragment != null) fragment.setColourSetListener(this);

        return super.onCreateView(inflater, container);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        nameEditText.setText(period.getName() != null ? period.getName() : "");
        periodNameInputLayout.setHint(getActivity().getString(R.string.edit_timetable_name));

        // region Set time views
        if (period.getStartTime() == null) {
            startTimeMainTextView.setText(getActivity().getString(R.string.date_not_set));
        } else {
            String[] startTime = TimeUtils.formatTime(period.getStartTime().getHourOfDay(), period.getStartTime().getMinuteOfHour()).split(Pattern.quote(" "));

            startTimeMainTextView.setText(startTime[0]);
            startTimeSecondTextView.setText(startTime[1]);
        }

        if (period.getEndTime() == null) {
            endTimeMainTextView.setText(getActivity().getString(R.string.date_not_set));
        } else {
            String[] endTime = TimeUtils.formatTime(period.getEndTime().getHourOfDay(), period.getEndTime().getMinuteOfHour()).split(Pattern.quote(" "));

            endTimeMainTextView.setText(endTime[0]);
            endTimeSecondTextView.setText(endTime[1]);
        }

        periodColourImage.setColorFilter(period.getColour() != 0 ? period.getColour() : ColourUtils.green);
        periodColourImage.setTag(period.getColour() != 0 ? period.getColour() : ColourUtils.green);

        if (periodPosition == -1) deletePeriodButton.setVisibility(View.GONE);
        deletePeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRouter().popCurrentController();
                editingFinishedListener.onDeletePeriodClicked(periodPosition);
            }
        });
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putParcelable("period", period);
        outState.putInt("position", periodPosition);
        outState.putInt("colour", (int) periodColourImage.getTag());
    }

    @Override
    protected void onRestore(Bundle inState) {
        period = inState.getParcelable("period");
        periodPosition = inState.getInt("position");

        ColourPickerDialogFragment fragment =
                (ColourPickerDialogFragment) getAppCombatActivity().getSupportFragmentManager().findFragmentByTag(FragmentTags.PERIOD_COLOUR_PICKER_DIALOG);

        if (fragment != null) fragment.setColourSetListener(this);

        if (periodColourImage != null) {
            int colour = inState.getInt("colour");
            periodColourImage.setColorFilter(colour);
            periodColourImage.setTag(colour);
        }
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public boolean handleBack() {
        getRouter().popCurrentController();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_done_edit) {
            String name = nameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                periodNameInputLayout.setError("The day needs a name!");
                return true;
            }

            period.setName(name);

            if (startTimeMainTextView.getText().equals(getActivity().getString(R.string.date_not_set)) ||
                    endTimeMainTextView.getText().equals(getActivity().getString(R.string.date_not_set))) {
                Toast.makeText(getApplicationContext(), "You need to set a time!", Toast.LENGTH_SHORT).show();
                return true;
            }

            period.setColour((int) periodColourImage.getTag());

            getRouter().popCurrentController();

            if (editingFinishedListener != null) editingFinishedListener.onFinishEditingPeriodClicked(period, periodPosition);
        }

        return true;
    }

    @Override
    protected String getTitle() {
        return period.getName() == null ? "New day" : "Edit " + period.getName();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("period", period);
        outState.putInt("position", periodPosition);
        outState.putInt("colour", (int) periodColourImage.getTag());

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.edit_period_start_time_view)
    void onStartTimeClicked() {
        int dialogStartHour = period.getStartTime() == null ? DateTime.now().getHourOfDay() : period.getStartTime().getHourOfDay();
        int dialogStartMinute = period.getStartTime() == null ? DateTime.now().getMinuteOfHour() : period.getStartTime().getMinuteOfHour();

        final TimePickerDialogFragment dialog = TimePickerDialogFragment.newInstance(new TimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(int hour, int minute) {
                period.setStartTime(new LocalTime(hour, minute));

                String[] time = TimeUtils.formatTime(hour, minute).split(Pattern.quote(" "));

                startTimeMainTextView.setText(time[0]);
                startTimeSecondTextView.setText(time[1]);
            }
        }, dialogStartHour, dialogStartMinute);

        dialog.show(getAppCombatActivity().getSupportFragmentManager(), "start_time");
    }

    @OnClick(R.id.edit_period_end_time_view)
    void onEndTimeClicked() {
        int dialogEndHour = period.getEndTime() == null ? DateTime.now().getHourOfDay() : period.getEndTime().getHourOfDay();
        int dialogEndMinute = period.getEndTime() == null ? DateTime.now().getMinuteOfHour() : period.getEndTime().getMinuteOfHour();

        TimePickerDialogFragment dialog = TimePickerDialogFragment.newInstance(new TimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(int hour, int minute) {
                period.setEndTime(new LocalTime(hour, minute));

                String[] time = TimeUtils.formatTime(hour, minute).split(Pattern.quote(" "));

                endTimeMainTextView.setText(time[0]);
                endTimeSecondTextView.setText(time[1]);
            }
        }, dialogEndHour, dialogEndMinute);

        dialog.show(getSupportFragmentManager(), "end_time");
    }

    @OnClick(R.id.colour_rect_image)
    void onColourRectClicked() {
        ColourPickerDialogFragment fragment =
                ColourPickerDialogFragment.newInstance((int) periodColourImage.getTag(), this);

        fragment.show(getSupportFragmentManager(), FragmentTags.PERIOD_COLOUR_PICKER_DIALOG);
    }

    @Override
    public void OnColourSet(@ColorInt int colour) {
        periodColourImage.setColorFilter(colour);
        periodColourImage.setTag(colour);
    }
}
