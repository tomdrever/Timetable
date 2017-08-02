package tomdrever.timetable.android.controllers.base;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

import tomdrever.timetable.android.controllers.ActionBarProvider;
import tomdrever.timetable.R;
import tomdrever.timetable.utils.TimetableFileManager;

public abstract class BaseController extends ButterKnifeController {

    protected BaseController() { }

    public BaseController(Bundle args) {
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

    protected AppCompatActivity getAppCombatActivity(){
        return (AppCompatActivity) getActivity();
    }

    protected FragmentManager getFragmentManager() {
        return getAppCombatActivity().getFragmentManager();
    }

    protected android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return getAppCombatActivity().getSupportFragmentManager();
    }

    protected abstract void onSave(Bundle outState);

    protected abstract void onRestore(Bundle inState);

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        onSave(outState);
        super.onSaveViewState(view, outState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        onSave(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        onRestore(savedViewState);
        super.onRestoreViewState(view, savedViewState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        onRestore(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
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