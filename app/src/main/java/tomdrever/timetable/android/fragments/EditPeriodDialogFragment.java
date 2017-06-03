package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utils.ColourUtils;
import tomdrever.timetable.utils.FragmentTags;
import tomdrever.timetable.utils.TimeUtils;

public class EditPeriodDialogFragment extends DialogFragment implements ColourPickerDialogFragment.OnColourSetListener {
	private Period period;

	private Unbinder unbinder;
	private View view;

	private int periodPosition;

    private PeriodDialogListener periodDialogListener;

    @BindView(R.id.period_name_input_layout) TextInputLayout periodNameInputLayout;

	@BindView(R.id.edit_period_name) EditText nameEditText;
    @BindView(R.id.edit_period_start_time_main_view) TextView startTimeMainTextView;
    @BindView(R.id.edit_period_start_time_second_view) TextView startTimeSecondTextView;
    @BindView(R.id.edit_period_end_time_main_view) TextView endTimeMainTextView;

    @BindView(R.id.edit_period_end_time_second_view) TextView endTimeSecondTextView;

	@BindView(R.id.colour_rect_image) ImageView periodColourImage;

	public static EditPeriodDialogFragment newInstance(Period period, int periodPosition,
													   PeriodDialogListener periodDialogListener) {
		EditPeriodDialogFragment fragment = new EditPeriodDialogFragment();
		fragment.period = period;
		fragment.periodPosition = periodPosition;
        fragment.periodDialogListener = periodDialogListener;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		unbinder = ButterKnife.bind(this, view);

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
		//endregion

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@NonNull
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            period = savedInstanceState.getParcelable("period");
            periodPosition = savedInstanceState.getInt("position");

            ColourPickerDialogFragment fragment =
                    (ColourPickerDialogFragment) getFragmentManager().findFragmentByTag(FragmentTags.PERIOD_COLOUR_PICKER_DIALOG);

            if (fragment != null) fragment.setColourSetListener(this);

            if (periodColourImage != null) {
                int colour = savedInstanceState.getInt("colour");
                periodColourImage.setColorFilter(colour);
                periodColourImage.setTag(colour);
            }
        }

		// region Set Up Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		view = inflater.inflate(R.layout.fragment_dialog_edit_period, null);

		builder.setView(view);

		builder.setTitle(periodPosition == -1 ? "New Period" : String.format("Edit %s", period.getName()));

		builder.setPositiveButton("OK", null);
		if (periodPosition != -1) builder.setNeutralButton("Delete", null);
		builder.setNegativeButton("Cancel", null);

		final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				// region On Positive Clicked
				Button posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				posButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Check the period has been given a name
						String name = nameEditText.getText().toString().trim();
						if (name.isEmpty()) {
							Toast.makeText(getActivity(), "The period needs a name!", Toast.LENGTH_SHORT).show();
							return;
						}
						period.setName(name);

						period.setColour((int)periodColourImage.getTag());

						if (period.getStartTime() == null) {
							Toast.makeText(getActivity(), "The period needs a start time!", Toast.LENGTH_SHORT).show();
							return;
						}

						if (period.getEndTime() == null) {
							Toast.makeText(getActivity(), "The period needs a end time!", Toast.LENGTH_SHORT).show();
							return;
						}

						if (periodDialogListener != null)
							periodDialogListener.onFinishEditingPeriodClicked(period, periodPosition);

						dismiss();
					}
				});
				// endregion

				// region On Neutral Clicked
				Button neutButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

				// If there is not a neutral button, do not attempt to give it an onclick;
				if (neutButton == null) return;

				neutButton.setTextColor(ContextCompat.getColor(getContext(), R.color.red));

				neutButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						periodDialogListener.onDeletePeriodClicked(periodPosition);
						dismiss();
					}
				});
				// endregion
			}
		});

        return dialog;
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

        dialog.show(getFragmentManager(), "start_time");
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

        dialog.show(getFragmentManager(), "end_time");
    }

	@OnClick(R.id.colour_rect_image)
	void onColourRectClicked() {
		ColourPickerDialogFragment fragment =
				ColourPickerDialogFragment.newInstance((int) periodColourImage.getTag(), this);

		fragment.show(getFragmentManager(), FragmentTags.PERIOD_COLOUR_PICKER_DIALOG);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
		unbinder = null;
	}

    @Override
    public void OnColourSet(@ColorInt int colour) {
        periodColourImage.setColorFilter(colour);
        periodColourImage.setTag(colour);
    }

    public void setPeriodDialogListener(PeriodDialogListener listener)
    {
        this.periodDialogListener = listener;
    }

    public interface PeriodDialogListener {
		void onFinishEditingPeriodClicked(Period period, int periodPosition);
		void onDeletePeriodClicked(int periodPosition);
	}
}
