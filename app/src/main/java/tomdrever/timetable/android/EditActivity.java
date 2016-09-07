package tomdrever.timetable.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.FragmentBackPressedListener;
import tomdrever.timetable.android.ui.edit.EditDayFragment;
import tomdrever.timetable.android.ui.edit.EditTimetableFragment;
import tomdrever.timetable.data.TimetableContainer;

public class EditActivity extends AppCompatActivity implements EditTimetableFragment.NewTimetableFinishedListener,
        FragmentBackPressedListener, EditTimetableFragment.DayClickedListener {
    // Fragments - edittimetable, edit day (?)
    // Launches - viewactivity on back (is not new) or overviewactivity (is new)

    private TimetableContainer timetableContainer;

    private boolean isNewTimetable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        Intent intent = getIntent();

        isNewTimetable = intent.getBooleanExtra("isnewtimetable", false);

        timetableContainer = new TimetableContainer((TimetableContainer) intent.getSerializableExtra("timetable"));

        transitionTo(EditTimetableFragment.newInstance(timetableContainer, isNewTimetable, this, this, this), false);
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
    public void OnNewTimetableFinished() {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("timetable", timetableContainer);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            Intent intent;

            if (isNewTimetable) {
                intent = new Intent(this, OverviewActivity.class);
            } else {
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("timetable", getIntent().getSerializableExtra("timetable"));
            }

            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDayClicked(int position) {
        transitionTo(EditDayFragment.newInstance(timetableContainer.getTimetable().getDays().get(position), this), true);
    }
}
