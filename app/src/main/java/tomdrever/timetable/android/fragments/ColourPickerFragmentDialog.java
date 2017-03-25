package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.utils.ViewUtils;

public class ColourPickerFragmentDialog extends DialogFragment {
    private OnColourSetListener colourSetListener;

    private Unbinder unbinder;
    private View view;

    @BindView(R.id.colour_picker_red) SeekBar redSeekBar;
    @BindView(R.id.colour_picker_green) SeekBar greenSeekBar;
    @BindView(R.id.colour_picker_blue) SeekBar blueSeekBar;

    private int red;
    private int green;
    private int blue;

    public static ColourPickerFragmentDialog newInstance(@ColorInt int colour,
                                                         OnColourSetListener listener) {
        ColourPickerFragmentDialog fragment = new ColourPickerFragmentDialog();
        fragment.colourSetListener = listener;
        fragment.red = Color.red(colour);
        fragment.green = Color.green(colour);
        fragment.blue = Color.blue(colour);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        ViewUtils.setUpSeekBar(redSeekBar, 255, red, ContextCompat.getColor(getContext(), R.color.red),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        red = seekBar.getProgress();
                    }
                }, getContext());

        ViewUtils.setUpSeekBar(greenSeekBar, 255, green, ContextCompat.getColor(getContext(), R.color.green),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        green = seekBar.getProgress();
                    }
                }, getContext());

        ViewUtils.setUpSeekBar(blueSeekBar, 255, blue, ContextCompat.getColor(getContext(), R.color.blue),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        blue = seekBar.getProgress();
                    }
                }, getContext());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            colourSetListener = (OnColourSetListener) savedInstanceState.getSerializable("listener");
        }

        // region Set Up Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.fragment_dialog_colour_picker, null);
        builder.setView(view);

        builder.setTitle("Pick a colour");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (savedInstanceState != null) {
                    red = redSeekBar.getProgress();
                    green = greenSeekBar.getProgress();
                    blue = blueSeekBar.getProgress();
                }

                // region On Positive Clicked
                Button posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        colourSetListener.OnColourSet(Color.rgb(red, green, blue));

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
        outState.putSerializable("listener", colourSetListener);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }

    public interface OnColourSetListener extends Serializable {
        void OnColourSet(@ColorInt int colour);
    }
}
