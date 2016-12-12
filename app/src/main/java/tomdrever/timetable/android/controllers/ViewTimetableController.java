package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;

import butterknife.BindView;
import butterknife.OnClick;
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

        testTextView.setText(timetable.getDescription());
    }

    @Override
    protected String getTitle() {
        return timetable.getName();
    }

    @Override
    public boolean handleBack() {
        getRouter().pushController(RouterTransaction.with(new TimetableListController()));

        return true;
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @OnClick(R.id.edit_timetable_fab)
    void onFabClicked() {
        // Todo - launch edit timetable
        getRouter().pushController(RouterTransaction.with(new EditTimetableController(timetable)));
    }
}
