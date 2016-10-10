package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Period;

public class EditPeriodDialogFragment extends DialogFragment {
	private Period period;
	private int periodPosition;

	private View dialogView;

	private FragmentSuccessListener successListener;

	public FragmentSuccessListener getSuccessListener() {
		return successListener;
	}

	public void setSuccessListener(FragmentSuccessListener successListener) {
		this.successListener = successListener;
	}

	public static EditPeriodDialogFragment newInstance(Period period, int periodPosition,
	                                                   FragmentSuccessListener successListener) {
		EditPeriodDialogFragment fragment = new EditPeriodDialogFragment();
		fragment.successListener = successListener;
		fragment.period = period;
		fragment.periodPosition = periodPosition;
		return fragment;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialogView = getActivity().getLayoutInflater().inflate(R.layout.edit_period_dialog, null);

		// region Set Up Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(dialogView);
		builder.setTitle(period == null ? "New Period" : String.format("Edit %s", period.getName()));
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", null);

		final AlertDialog dialog = builder.create();
		// endregion

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						EditText editPeriodName = (EditText) dialogView.findViewById(R.id.edit_period_name_edit_text);
						// Check the period has been given a name
						String name = editPeriodName.getText().toString().trim();
						if (name.isEmpty()) {
							// If not, tell the user
							Toast.makeText(getContext(), "The period needs a name!", Toast.LENGTH_SHORT).show();
						} else {
							period.setName(name);

							successListener.onFragmentSuccess(period, periodPosition);
							dismiss();
						}
					}
				});
			}
		});

        return dialog;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// region Set Up UI and Period
		if (period == null) period = new Period("New Period");

		((EditText) dialogView.findViewById(R.id.edit_period_name_edit_text)).setText(period.getName());
		// endregion
	}

	interface FragmentSuccessListener {
	    void onFragmentSuccess(Period period, int periodPosition);
    }
}
