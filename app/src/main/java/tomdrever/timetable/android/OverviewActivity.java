package tomdrever.timetable.android;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.view.TimetablesOverviewFragment;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.data.TimetableContainer;

import java.util.Calendar;

public class OverviewActivity extends AppCompatActivity implements TimetablesOverviewFragment.CardClickedListener, TimetablesOverviewFragment.NewTimetableClickListener {
    // Fragments - overview fragment
    // Launches - editactivity on "new" fab clicked
    // Launches -  viewactivity on item clicked

    private ObservableArrayList<TimetableContainer> timetables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        TimetableFileManager fileManager = new TimetableFileManager(this);

        timetables = new ObservableArrayList<>();
        timetables.addAll(fileManager.loadAll());

        transitionTo(TimetablesOverviewFragment.newInstance(timetables, this, this), false);
    }

    private void transitionTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCardClicked(int cardPosition) {
        Intent intent = new Intent(this, ViewActivity.class);
        // TODO - create refs static class to hold intent extra names
        intent.putExtra("timetable", timetables.get(cardPosition));
        startActivity(intent);
        finish();
    }

    @Override
    public void onNewTimetableClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("isnewtimetable", true);
        intent.putExtra("timetable", new TimetableContainer("", "", Calendar.getInstance().getTime(), new Timetable()));
        startActivity(intent);
        finish();
    }
}
