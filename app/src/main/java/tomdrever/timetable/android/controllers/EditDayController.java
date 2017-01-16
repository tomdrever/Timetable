package tomdrever.timetable.android.controllers;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tomdrever.timetable.R;
import tomdrever.timetable.android.EditingFinishedListener;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.android.fragments.EditPeriodDialogFragment;
import tomdrever.timetable.android.views.ExpandableRecyclerView;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.Period;
import tomdrever.timetable.utility.CollectionUtils;

public class EditDayController extends BaseController {

    private Day day;
    private ArrayList<Period> periods;

    private PeriodListAdapter periodListAdapter;

    private EditingFinishedListener editingFinishedListener;

    public void setEditingFinishedListener(EditingFinishedListener editingFinishedListener) {
        this.editingFinishedListener = editingFinishedListener;
    }

    @BindView(R.id.day_name_input_layout) TextInputLayout dayNameInputLayout;
    @BindView(R.id.edit_day_name) EditText dayNameEditText;
    @BindView(R.id.edit_periods_recyclerview) ExpandableRecyclerView periodsRecyclerView;

    @BindView(R.id.edit_day_scrollview) ScrollView editDayScrollView;
    @BindView(R.id.new_period_fab) FloatingActionButton newPeriodButton;

    @BindView(R.id.no_periods) TextView noPeriodsTextView;

    public EditDayController() {}

    public EditDayController(Day day) {
        this.day = day;
        this.periods = CollectionUtils.copyPeriods(day.getPeriods());
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_day, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        dayNameEditText.setText(day.getName() != null ? day.getName() : "");
        dayNameInputLayout.setHint(getActivity().getString(R.string.edit_day_name));

        periodListAdapter = new PeriodListAdapter(LayoutInflater.from(view.getContext()));

        periodsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
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

        updateNoPeriodsTextView();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public boolean handleBack() {
        String name = dayNameEditText.getText().toString().trim();

        if (!day.equals(newDay(name))) {
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
            day.setPeriods(periods);

            if (editingFinishedListener != null) editingFinishedListener.onEditingFinished();

            getRouter().popCurrentController();
        }

        return true;
    }

    @OnClick(R.id.new_period_fab)
    void onNewPeriodClicked() {
        Toast.makeText(getActivity(), "New period clicked", Toast.LENGTH_SHORT).show();
    }

    private void addPeriod(Period period) {
        // TODO - reorder periods chronologically
    }

    private void removePeriodAt(int position) {}

    private void updateNoPeriodsTextView() {
        if (periods.isEmpty())
            noPeriodsTextView.setVisibility(View.VISIBLE);
        else
            noPeriodsTextView.setVisibility(View.GONE);
    }

    private Day newDay(String name) {
        Day newDay = new Day(day);
        newDay.setName(name);
        newDay.setPeriods(periods);

        return newDay;
    }

    @Override
    protected String getTitle() {
        return day.getName() == null ? "New day" : "Edit " + day.getName();
    }

    class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.PeriodViewHolder> {
        private final LayoutInflater inflater;

        public PeriodListAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PeriodViewHolder(inflater.inflate(R.layout.period_card, parent, false));
        }

        @Override
        public void onBindViewHolder(PeriodViewHolder holder, int position) {
            holder.bind(periods.get(position));
        }

        @Override
        public int getItemCount() {
            return periods.size();
        }

        class PeriodViewHolder extends RecyclerView.ViewHolder {

            Period period;

            @BindView(R.id.period_name_text) TextView nameTextView;

            public PeriodViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            public void bind(Period item) {
                this.period = item;

                nameTextView.setText(period.getName());
            }

            @OnClick(R.id.period_card_base_view)
            void onCardClicked() {
                EditingFinishedListener listener = new EditingFinishedListener() {
                    @Override
                    public void onEditingFinished() {
                        notifyDataSetChanged();
                    }
                };

                EditPeriodDialogFragment periodFragment =
                        EditPeriodDialogFragment.newInstance(period, getAdapterPosition(), listener);
                FragmentManager fm = getAppCombatActivity().getSupportFragmentManager();

                if (fm != null)
                    periodFragment.show(fm, "period_fragment");
            }
        }
    }
}
