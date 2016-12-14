package tomdrever.timetable.android.controllers.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import tomdrever.timetable.android.ActionBarProvider;
import tomdrever.timetable.R;
import tomdrever.timetable.utility.TimetableFileManager;

public abstract class BaseController extends ButterKnifeController {

    protected BaseController() { }

    protected BaseController(Bundle args) {
        super(args);
    }

    // REM - apparently this isn't good, but it's copied from the demo app, so I hope it works
    protected ActionBar getActionBar() {
        ActionBarProvider actionBarProvider = ((ActionBarProvider)getActivity());
        return actionBarProvider != null ? actionBarProvider.getSupportActionBar() : null;
    }

    private TimetableFileManager fileManager;
    protected TimetableFileManager getFileManager() {
        if (fileManager == null) fileManager = new TimetableFileManager(getApplicationContext());
        return fileManager;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        setTitle();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        if (showUpNavigation()) {
            getActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRouter().handleBack();
                }
            });
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(false);

            toolbar.setNavigationOnClickListener(null);
        }

        super.onAttach(view);
    }

    protected void setTitle() {
        Controller parentController = getParentController();
        while (parentController != null) {
            if (parentController instanceof BaseController && ((BaseController)parentController).getTitle() != null) {
                return;
            }
            parentController = parentController.getParentController();
        }

        String title = getTitle();
        ActionBar actionBar = getActionBar();
        if (title != null && actionBar != null) {
            actionBar.setTitle(title);

            String subtitle = getSubtitle();
            if (subtitle != null) {
                actionBar.setSubtitle(subtitle);
            } else {
                actionBar.setSubtitle("");
            }
        }
    }

    // Override this with true to show the "back" arrow on the toolbar and have it act as a back button
    protected boolean showUpNavigation() {
        return false;
    }

    protected String getTitle() {
        return null;
    }

    protected String getSubtitle() {
        return null;
    }
}