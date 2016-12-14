package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import butterknife.BindView;
import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;
import tomdrever.timetable.data.Timetable;

public class ViewTimetableController extends BaseController {

    private Timetable timetable;

    @BindView(R.id.text_view_test) TextView testTextView;

    public ViewTimetableController() {}
    public ViewTimetableController(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_view_timetable, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        setHasOptionsMenu(true);

        testTextView.setText(timetable.getDescription());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_view_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit_view) {
            getRouter().pushController(RouterTransaction.with(new EditTimetableController(timetable))
                    .popChangeHandler(new FadeChangeHandler())
                    .pushChangeHandler(new FadeChangeHandler()));
        }

        return true;
    }

    @Override
    protected String getTitle() {
        return timetable.getName();
    }

    @Override
    protected String getSubtitle() {
        return timetable.getDescription();
    }

    @Override
    public boolean handleBack() {
        getRouter().pushController(RouterTransaction.with(new TimetableListController())
                .popChangeHandler(new FadeChangeHandler())
                .pushChangeHandler(new FadeChangeHandler()));

        return true;
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }
}
