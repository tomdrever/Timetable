package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import tomdrever.timetable.R;
import tomdrever.timetable.data.Period;

public class EditPeriodDialogFragment extends DialogFragment {
	private Period period;
	private int periodPosition;

	private View view;

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
		view = getActivity().getLayoutInflater().inflate(R.layout.edit_period_dialog, null);

		// region Set Up Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		builder.setTitle(period == null ? "New Period" : String.format("Edit %s", period.getName()));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				EditText editPeriodName = (EditText) view.findViewById(R.id.edit_period_name_edit_text);
				// Check the timetable has been given a name
				String name = editPeriodName.getText().toString().trim();
				if (name.isEmpty()) {
					// If not, tell the user
					Toast.makeText(getContext(), "The period needs a name!", Toast.LENGTH_SHORT).show();

					// TODO - highlight name field to the user in some way - USER, DO THIS!
				} else {
					successListener.onFragmentSuccess(period, periodPosition);
				}
			}
		});
		builder.setNegativeButton("Cancel", null);
		// endregion

        return builder.create();
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// region Set Up UI and Period
		if (period == null) period = new Period("New Period");

		((EditText) view.findViewById(R.id.edit_period_name_edit_text)).setText(period.getName());
		// endregion
	}

	interface FragmentSuccessListener {
	    void onFragmentSuccess(Period period, int periodPosition);
    }
}
