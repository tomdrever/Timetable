package tomdrever.timetable.android.controllers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.android.fragments.ColourPickerDialogFragment;
import tomdrever.timetable.android.fragments.EditPeriodDialogFragment;
import tomdrever.timetable.android.views.ExpandableRecyclerView;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utils.CollectionUtils;
import tomdrever.timetable.utils.ColourUtils;
import tomdrever.timetable.utils.FragmentTags;
import tomdrever.timetable.utils.TimeUtils;

public class EditDayController extends BaseController implements EditPeriodDialogFragment.PeriodDialogListener, ColourPickerDialogFragment.OnColourSetListener {

    private Day day;

    private EditingFinishedListener editingFinishedListener;

    public void setEditingFinishedListener(EditingFinishedListener editingFinishedListener) {
        this.editingFinishedListener = editingFinishedListener;
    }

    @BindView(R.id.day_name_input_layout) TextInputLayout dayNameInputLayout;

    @BindView(R.id.edit_day_name) EditText dayNameEditText;
    @BindView(R.id.colour_rect_image) ImageView dayColourImage;

    @BindView(R.id.edit_periods_recyclerview) ExpandableRecyclerView periodsRecyclerView;

    private PeriodListAdapter periodListAdapter;

    @BindView(R.id.edit_day_scrollview) ScrollView editDayScrollView;

    @BindView(R.id.new_period_fab) FloatingActionButton newPeriodButton;

    @BindView(R.id.no_periods) TextView noPeriodsTextView;

    public EditDayController() {}

    public EditDayController(Day day) {
        this.day = day;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_day, container, false);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        ColourPickerDialogFragment fragment =
                (ColourPickerDialogFragment) getAppCombatActivity().getSupportFragmentManager().findFragmentByTag(FragmentTags.DAY_COLOUR_PICKER_DIALOG);

        if (fragment != null) fragment.setColourSetListener(this);

        return super.onCreateView(inflater, container);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        dayNameEditText.setText(day.getName() != null ? day.getName() : "");
        dayNameInputLayout.setHint(getActivity().getString(R.string.edit_day_name));

        // If the day's colour is 0 (black?), set the colour to default green. otherwise, use the day's colour
        dayColourImage.setColorFilter(day.getColour() != 0 ? day.getColour() : ColourUtils.green);
        dayColourImage.setTag(day.getColour() != 0 ? day.getColour() : ColourUtils.green);

        periodListAdapter = new PeriodListAdapter(LayoutInflater.from(view.getContext()), this, day.getPeriods());

        periodsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically() { return false; }
        });

        periodsRecyclerView.setAdapter(periodListAdapter);

        editDayScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int scrollY = editDayScrollView.getScrollY();

                if (scrollY != 0) {
                    newPeriodButton.hide();
                } else {
                    newPeriodButton.show();
                }
            }
        });

        periodListAdapter.updateNoPeriodsTextView();
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putParcelable("day", day);
        outState.putInt("colour", (int) dayColourImage.getTag());
    }

    @Override
    protected void onRestore(Bundle inState) {
        day = inState.getParcelable("day");

        if (dayColourImage != null) {
            int colour = inState.getInt("colour");
            dayColourImage.setColorFilter(colour);
            dayColourImage.setTag(colour);
        }

        // Re-establish this as the PeriodDialogListener
        EditPeriodDialogFragment dialogFragment = (EditPeriodDialogFragment) getAppCombatActivity().getSupportFragmentManager()
                .findFragmentByTag(FragmentTags.PERIOD_FRAGMENT);
        if (dialogFragment != null) {
            dialogFragment.setPeriodDialogListener(this);
        }
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public boolean handleBack() {
        String name = dayNameEditText.getText().toString().trim();

        if (Objects.equals(name, "")) name = null;

        Day newDay = newDay(name);

        if (!day.equals(newDay)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Discard changes?");

            //region Buttons

            // Add the buttons
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Discard changes
                    getRouter().popCurrentController();
                }
            });

            builder.setNegativeButton("Cancel", null);

            // endregion

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            getRouter().popCurrentController();

            return true;
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_day, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_done_edit) {
            // NOTE - Simply "done". File saving is ONLY done in edittimetable

            String name = dayNameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "The day needs a name!", Toast.LENGTH_SHORT).show();
                return true;
            }

            day.setName(name);
            day.setPeriods(periodListAdapter.getItems());
            day.setColour((int) dayColourImage.getTag());

            if (editingFinishedListener != null) editingFinishedListener.onEditingFinished();

            getRouter().popCurrentController();
        }

        return true;
    }

    @OnClick(R.id.new_period_fab)
    void onNewPeriodClicked() {
        FragmentManager fm = getAppCombatActivity().getSupportFragmentManager();

        EditPeriodDialogFragment periodFragment =
                EditPeriodDialogFragment.newInstance(new Period(), -1, this);

        periodFragment.show(fm, FragmentTags.PERIOD_FRAGMENT);
    }

    @OnClick(R.id.colour_rect_image)
    void onColourRectClicked() {
        FragmentManager fm = getAppCombatActivity().getSupportFragmentManager();

        ColourPickerDialogFragment fragment =
                ColourPickerDialogFragment.newInstance((int) dayColourImage.getTag(), this);

        fragment.show(fm, FragmentTags.DAY_COLOUR_PICKER_DIALOG);
    }

    private Day newDay(String name) {
        Day newDay = day.cloneItem();
        newDay.setName(name);
        newDay.setPeriods(periodListAdapter.getItems());
        newDay.setColour((int) dayColourImage.getTag());

        return newDay;
    }

    @Override
    protected String getTitle() {
        return day.getName() == null ? "New day" : "Edit " + day.getName();
    }

    @Override
    public void onFinishEditingPeriodClicked(Period period, int position) {
        if (position == -1) {
            periodListAdapter.add(period);
            return;
        }

        periodListAdapter.set(period, position);
        periodListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeletePeriodClicked(final int periodPosition) {
        final Period period = periodListAdapter.getItemAt(periodPosition);

        periodListAdapter.remove(period);

        Snackbar.make(periodsRecyclerView, period.getName() + " deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restore
                periodListAdapter.addAt(period, periodPosition);
            }
        }).show();
    }

    @Override
    public void OnColourSet(@ColorInt int colour) {
        dayColourImage.setColorFilter(colour);
        dayColourImage.setTag(colour);
    }

    class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.PeriodViewHolder> {
        private final LayoutInflater inflater;
        private EditPeriodDialogFragment.PeriodDialogListener listener;
        private ArrayList<Period> items;

        public PeriodListAdapter(LayoutInflater inflater, EditPeriodDialogFragment.PeriodDialogListener listener,
                                 ArrayList<Period> periods) {
            this.inflater = inflater;
            this.listener = listener;
            this.items = periods;
        }

        @Override
        public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PeriodViewHolder(inflater.inflate(R.layout.view_card_edit_period, parent, false));
        }

        @Override
        public void onBindViewHolder(PeriodViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public Period getItemAt(int position) {
            return items.get(position);
        }

        public ArrayList<Period> getItems() {
            return items;
        }

        public void set(Period period, int position)
        {
            items.set(position, period);

            notifyDataSetChanged();
        }

        public void add(Period period) {
            items.add(period);
            CollectionUtils.sortPeriods(items);

            notifyDataSetChanged();
            updateNoPeriodsTextView();
        }

        public void addAt(Period period, int position) {
            items.add(position, period);

            notifyDataSetChanged();
            updateNoPeriodsTextView();
        }

        public void remove(Period period) {
            items.remove(period);

            notifyDataSetChanged();
            updateNoPeriodsTextView();
        }

        public void updateNoPeriodsTextView() {
            if (items.isEmpty())
                noPeriodsTextView.setVisibility(View.VISIBLE);
            else
                noPeriodsTextView.setVisibility(View.GONE);
        }

        class PeriodViewHolder extends RecyclerView.ViewHolder {

            Period period;

            @BindView(R.id.period_name_text) TextView nameTextView;
            @BindView(R.id.period_start_text) TextView startTimeTextView;
            @BindView(R.id.period_end_text) TextView endTimeTextView;

            public PeriodViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            public void bind(Period item) {
                this.period = item;

                nameTextView.setText(period.getName());

                startTimeTextView.setText(TimeUtils.formatTime(period.getStartTime().getHourOfDay(),
                                                                period.getStartTime().getMinuteOfHour()));

                endTimeTextView.setText(TimeUtils.formatTime(period.getEndTime().getHourOfDay(),
                        period.getEndTime().getMinuteOfHour()));
            }

            @OnClick(R.id.period_card_base_view)
            void onCardClicked() {

                FragmentManager fm = getAppCombatActivity().getSupportFragmentManager();

                EditPeriodDialogFragment periodFragment =
                        EditPeriodDialogFragment.newInstance(period.cloneItem(), getAdapterPosition(), listener);

                periodFragment.show(fm, FragmentTags.PERIOD_FRAGMENT);
            }
        }
    }
}
