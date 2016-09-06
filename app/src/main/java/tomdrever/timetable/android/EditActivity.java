package tomdrever.timetable.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.edit.EditTimetableFragment;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.data.TimetableContainer;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements EditTimetableFragment.NewTimetableFinishedListener,
        EditTimetableFragment.EditBackPressedListener {
    // Fragments - edittimetable, edit day (?)
    // Launches - viewactivity on back (is not new) or overviewactivity (is new)

    private TimetableContainer timetableContainer;

    private boolean isNewTimetable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        isNewTimetable = intent.getBooleanExtra("isnewtimetable", false);

        if (!isNewTimetable) timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetable");
        else {
            timetableContainer = new TimetableContainer("", "", Calendar.getInstance().getTime(), new Timetable());
        }


        transitionTo(EditTimetableFragment.newInstance(timetableContainer, isNewTimetable, this));
    }

    private void transitionTo(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void OnNewTimetableFinished() {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("timetable", timetableContainer);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEditBackPressed() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
        finish();
    }
}
