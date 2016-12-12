package tomdrever.timetable.android.controllers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tomdrever.timetable.R;
import tomdrever.timetable.android.controllers.base.BaseController;

public class ViewTimetableController extends BaseController {

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_view_timetable, container, false);
    }
}
