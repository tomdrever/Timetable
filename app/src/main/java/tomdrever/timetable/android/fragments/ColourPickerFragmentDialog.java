package tomdrever.timetable.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import tomdrever.timetable.R;
import tomdrever.timetable.utils.ColourUtils;
import tomdrever.timetable.utils.ViewUtils;

public class ColourPickerFragmentDialog extends DialogFragment {
    private OnColourSetListener colourSetListener;

    private Unbinder unbinder;
    private View view;

    @BindView(R.id.colour_picker_red) SeekBar redSeekBar;
    @BindView(R.id.colour_picker_green) SeekBar greenSeekBar;
    @BindView(R.id.colour_picker_blue) SeekBar blueSeekBar;

    @BindView(R.id.colour_picker_list) RecyclerView colourList;
    private ColourListAdapter colourListAdapter;

    @BindView(R.id.new_colour_name) TextInputEditText newColourNameEditText;
    @BindView(R.id.new_colour_name_layout) TextInputLayout newColourNameLayout;

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

        newColourNameLayout.setHint("New colour name");

        ViewUtils.setUpSeekBar(redSeekBar, 255, red, ContextCompat.getColor(getContext(), R.color.red),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        red = seekBar.getProgress();
                    }
                });

        ViewUtils.setUpSeekBar(greenSeekBar, 255, green, ContextCompat.getColor(getContext(), R.color.green),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        green = seekBar.getProgress();
                    }
                });

        ViewUtils.setUpSeekBar(blueSeekBar, 255, blue, ContextCompat.getColor(getContext(), R.color.blue),
                new OnSeekBarChangeListener(){
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        blue = seekBar.getProgress();
                    }
                });

        colourListAdapter = new ColourListAdapter(inflater, getContext());
        colourList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        colourList.setAdapter(colourListAdapter);

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

        builder.setNeutralButton("Create New", null);
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

                // region Save Custom Colour
                Button saveColourButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                saveColourButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));

                saveColourButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the new colour data (rgb + name)
                        String name = newColourNameEditText.getText().toString();

                        if (name.trim().isEmpty()) {
                            Toast.makeText(getContext(), "The new colour needs a name!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int colour = Color.rgb(red, green, blue);

                        ColourUtils.addCustomColour(name, colour, getContext());

                        colourSetListener.OnColourSet(colour);
                        dismiss();
                    }
                });
                // endregion
            }
        });
        // endregion

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

    public class ColourListAdapter extends RecyclerView.Adapter<ColourListAdapter.ColourItemViewHolder> {
        private LayoutInflater inflater;
        private HashMap<String, Integer> items;

        public ColourListAdapter(LayoutInflater inflater, Context context) {
            this.inflater = inflater;
            this.items = ColourUtils.loadCustomColours(context);
        }

        @Override
        public ColourItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ColourItemViewHolder(inflater.inflate(R.layout.view_colour_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ColourItemViewHolder holder, int position) {
            String name = items.keySet().toArray(new String[]{})[position];
            int colour = items.get(name);

            holder.bind(name, colour);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ColourItemViewHolder extends RecyclerView.ViewHolder {
            String name;
            int colour;

            @BindView(R.id.colour_item_name) TextView colourNameView;
            @BindView(R.id.colour_item_circle_image) ImageView colourCircleView;

            public ColourItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(final String name, final int colour) {
                this.name = name;
                colourNameView.setText(name);

                this.colour = colour;
                colourCircleView.setColorFilter(colour);
                colourCircleView.setImageResource(R.drawable.circle);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        colourSetListener.OnColourSet(colour);
                        dismiss();
                    }
                };

                colourNameView.setOnClickListener(listener);
                colourCircleView.setOnClickListener(listener);

                colourNameView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        newColourNameEditText.setText(name);
                        return true;
                    }
                });
            }

            @OnClick(R.id.delete_colour_icon)
            void onDeleteClicked() {
                ColourUtils.removeCustomColour(name, colour, getContext());
                items.remove(name);
                notifyDataSetChanged();
                Snackbar.make(getActivity().getCurrentFocus(), name + " deleted", Snackbar.LENGTH_SHORT).setAction(
                    "Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Restore
                            ColourUtils.addCustomColour(name, colour, getContext());
                            items.put(name, colour);
                            notifyDataSetChanged();
                        }
                    }).show();
            }
        }
    }
}
