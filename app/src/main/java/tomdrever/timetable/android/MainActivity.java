package tomdrever.timetable.android;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.view.TimetablesOverviewFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TimetablesOverviewFragment fragment = new TimetablesOverviewFragment();
        fragmentTransaction.add(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }
}
