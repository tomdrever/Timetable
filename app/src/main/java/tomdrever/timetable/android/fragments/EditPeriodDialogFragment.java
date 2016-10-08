package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import tomdrever.timetable.data.Period;

public class EditPeriodDialogFragment extends DialogFragment {
	private Period period;
	private int periodPosition;

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
		// region Set Up Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(period == null ? "New Period" : String.format("Edit %s", period.getName()));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				successListener.onFragmentSuccess(period, periodPosition);
			}
		});
		builder.setNegativeButton("Cancel", null);
		// endregion

		if (period == null) period = new Period("New Period");

        return builder.create();
    }

    interface FragmentSuccessListener {
	    void onFragmentSuccess(Period period, int periodPosition);
    }
}
