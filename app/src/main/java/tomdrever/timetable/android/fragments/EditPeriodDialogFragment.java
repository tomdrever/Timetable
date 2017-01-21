package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Period;

public class EditPeriodDialogFragment extends DialogFragment {
	private Period period;

	private Unbinder unbinder;
	private View view;

	private int periodPosition;

	@BindView(R.id.period_name_input_layout) TextInputLayout periodNameInputLayout;
	@BindView(R.id.edit_period_name) EditText nameEditText;

    private PeriodDialogListener periodDialogListener;

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
		if (period == null) period = new Period("New Period");

		unbinder = ButterKnife.bind(this, view);

		nameEditText.setText(period.getName() != null ? period.getName() : "");
		periodNameInputLayout.setHint(getActivity().getString(R.string.edit_timetable_name));

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@NonNull
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		// region Set Up Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		view = inflater.inflate(R.layout.edit_period_dialog, null);

		builder.setView(view);

		builder.setTitle(period == null ? "New Period" : String.format("Edit %s", period.getName()));

		builder.setPositiveButton("OK", null);
		if (period != null) builder.setNeutralButton("Delete", null);
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
							// If not, tell the user
							Toast.makeText(getActivity(), "The period needs a name!", Toast.LENGTH_SHORT).show();
						} else {
							period.setName(name);
							if (periodDialogListener != null)
								periodDialogListener.onFinishEditingPeriodClicked();

							dismiss();
						}
					}
				});
				// endregion

				// region On Neutral Clicked
				Button neutButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

				// If there is not a neutral button, do not attempt to give it an onclick;
				if (neutButton == null) return;

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
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
		unbinder = null;
	}

	public interface PeriodDialogListener {
		void onFinishEditingPeriodClicked();
		void onDeletePeriodClicked(int periodPosition);
	}
}
