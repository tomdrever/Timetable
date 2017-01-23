package tomdrever.timetable.android.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragmentDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public OnTimeSetListener timeSetListener;

    public int hour;
    public int minute;

    public static TimePickerFragmentDialog newInstance(OnTimeSetListener listener, int hour, int minute) {
        TimePickerFragmentDialog fragment = new TimePickerFragmentDialog();

        fragment.timeSetListener = listener;

        fragment.hour = hour;
        fragment.minute = minute;

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        // Do something with the time chosen by the user
        timeSetListener.onTimeSet(hour, minute);
    }

    public interface OnTimeSetListener {
        void onTimeSet(int hour, int minute);
    }
}
