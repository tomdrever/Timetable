package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;

import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Timetable;

public class EditTimetableController extends BaseController {

    private Timetable timetable;

    public EditTimetableController() {
        // Is new timetable!
        timetable = new Timetable();
        timetable.setName("New timetable");
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


    }

    // TODO - fix menus
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_timetable, menu);
    }

    // TODO - on menu click, save timetable (w/ days, name and description)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_edit) {
            // Done - launch view of that timetable
            getRouter().pushController(RouterTransaction.with(new ViewTimetableController(timetable)));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "Edit " + timetable.getName();
    }
}
