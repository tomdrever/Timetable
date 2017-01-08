package tomdrever.timetable.android.controllers;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Day;

public class EditDayController extends BaseController {

    private Day day;

    private OnControllerFinished onControllerFinished;

    public void setOnControllerFinished(OnControllerFinished onControllerFinished) {
        this.onControllerFinished = onControllerFinished;
    }

    @BindView(R.id.day_name_input_layout) TextInputLayout dayNameInputLayout;
    @BindView(R.id.edit_day_name) EditText dayNameEditText;

    public EditDayController() {}

    public EditDayController(Day day) {
        this.day = day;
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
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    // TODO - fix navigation (preferably with a universal system)
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

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

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

            if (onControllerFinished != null) onControllerFinished.run();

            getRouter().popCurrentController();
        }

        return true;
    }

    private Day newDay(String name) {
        Day newDay = new Day(day);
        day.setName(name);

        return newDay;
    }

    @Override
    protected String getTitle() {
        return day.getName() == null ? "New day" : "Edit " + day.getName();
    }
}
