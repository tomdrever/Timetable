package tomdrever.timetable.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.FragmentBackPressedListener;
import tomdrever.timetable.android.ui.view.ViewTimetableFragment;
import tomdrever.timetable.data.TimetableContainer;

public class ViewActivity extends AppCompatActivity implements FragmentBackPressedListener, ViewTimetableFragment.ViewEditPressedListener {
    // Fragments - viewtimetable, view day (?)
    // Launches - editactivity on "edit" fab
    // "Launches" - overviewactivity, on back

    private TimetableContainer timetableContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        Intent intent = getIntent();

        timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetable");

        transitionTo(ViewTimetableFragment.newInstance(timetableContainer, this, this), false);
    }

    private void transitionTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        onFragmentBackPressed();
    }

    @Override
    public void onFragmentBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Intent intent = new Intent(this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onViewEditPressed() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("isnewtimetable", false);
        intent.putExtra("timetable", timetableContainer);
        startActivity(intent);
        finish();
    }
}
