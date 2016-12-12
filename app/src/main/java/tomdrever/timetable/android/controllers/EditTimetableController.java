package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Timetable;

public class EditTimetableController extends BaseController {

    private Timetable timetable;

    @BindView(R.id.edit_timetable_name) TextView nameTextView;
    @BindView(R.id.edit_timetable_description) TextView descriptionTextView;

    public EditTimetableController() { }

    public EditTimetableController(int index) {
        // Is new timetable!
        timetable = new Timetable();
        timetable.setIndex(index);
    }

    public EditTimetableController(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public boolean handleBack() {
        // TODO - "Discard changes?"
        return super.handleBack();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_edit_timetable, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        nameTextView.setText(timetable.getName() != null ? timetable.getName() : "");
        descriptionTextView.setText(timetable.getDescription() != null ? timetable.getDescription() : "");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_timetable, menu);
    }

    // TODO - on menu click, save timetable (w/ days, name and description and INDEX)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_edit) {
            // Save timetable
            // Check input
            String name = nameTextView.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "The timetable needs a name!", Toast.LENGTH_SHORT).show();
            } else {
                String description = descriptionTextView.getText().toString().trim();

                // If this is an existing timetable, get rid of the old version before saving
                if (timetable.getName() != null) getFileManager().delete(timetable.getName());

                timetable.setName(name);
                timetable.setDescription(description);

                getFileManager().save(timetable);

                // Done - launch view of that timetable
                getRouter().pushController(RouterTransaction.with(new ViewTimetableController(timetable))
                        .popChangeHandler(new FadeChangeHandler())
                        .pushChangeHandler(new FadeChangeHandler()));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    protected String getTitle() {
        return timetable.getName() != null ? "Edit " + timetable.getName() : "Create Timetable";
    }
}
