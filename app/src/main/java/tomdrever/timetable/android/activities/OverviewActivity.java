package tomdrever.timetable.android.activities;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import tomdrever.timetable.R;
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.android.fragments.TimetablesOverviewFragment;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.utility.IntentExtraTags;

import java.util.Calendar;

public class OverviewActivity extends AppCompatActivity implements TimetablesOverviewFragment.TimetableClickedListener,
        TimetablesOverviewFragment.NewTimetableClickListener {

    private ObservableArrayList<TimetableContainer> timetableContainers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        TimetableFileManager fileManager = new TimetableFileManager(this);

        timetableContainers = new ObservableArrayList<>();
        timetableContainers.addAll(fileManager.loadAll());

        transitionTo(TimetablesOverviewFragment.newInstance(timetableContainers, this, this), false);
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
    public void onTimetableClicked(int cardPosition) {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra(IntentExtraTags.TIMETABLECONTAINER, timetableContainers.get(cardPosition));
        startActivity(intent);
        finish();
    }

    @Override
    public void onNewTimetableClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(IntentExtraTags.ISNEWTIMETABLE, true);
        intent.putExtra(IntentExtraTags.TIMETABLECONTAINER, new TimetableContainer("", "", Calendar.getInstance().getTime(), new Timetable(),
                timetableContainers.size()));
        startActivity(intent);
        finish();
    }
}
