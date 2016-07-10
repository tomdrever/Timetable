package tomdrever.timetable.android.ui.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.edit.EditTimetableActivity;
import tomdrever.timetable.databinding.ActivityViewTimetableBinding;
import tomdrever.timetable.data.TimetableContainer;

public class ViewTimetableActivity extends AppCompatActivity {
    public static final int EDIT_NEW_TIMETABLE_CODE = 200;

    private TimetableContainer timetableContainer; // TODO - make observable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get timetable
        Intent intent = getIntent();
        timetableContainer = new Gson().fromJson(intent.getStringExtra("timetabledetailsjson"), TimetableContainer.class);

        // Bind timetable to layout
        ActivityViewTimetableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_timetable);
        binding.setTimetable(timetableContainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_timetable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(100);
                    supportFinishAfterTransition();
                }
            });
        }

        // If we know it is a new timetable, immediately launch edit timetable, for creation.
        if (intent.getBooleanExtra("isnewtimetable", false)){
            Intent launchEditIntent = new Intent(this, EditTimetableActivity.class);
            launchEditIntent.putExtra("timetabledetailsjson", new Gson().toJson(timetableContainer));
            startActivityForResult(launchEditIntent, EDIT_NEW_TIMETABLE_CODE);
        }

        FloatingActionButton editTimetableFAB = (FloatingActionButton)findViewById(R.id.edit_timetable_fab);

        if (editTimetableFAB != null) {
            editTimetableFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchEditIntent = new Intent(getBaseContext(), EditTimetableActivity.class);
                    launchEditIntent.putExtra("timetabledetailsjson", new Gson().toJson(timetableContainer));
                    startActivityForResult(launchEditIntent, EDIT_NEW_TIMETABLE_CODE);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == EDIT_NEW_TIMETABLE_CODE) { // Editing has finished
                // Unpack data and set to timetable object
                String timetablejson = data.getStringExtra("timetabledetailsjson");
                timetableContainer = new Gson().fromJson(timetablejson, TimetableContainer.class);

                Snackbar.make(findViewById(android.R.id.content), String.format("Saved %s", timetableContainer.name), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
