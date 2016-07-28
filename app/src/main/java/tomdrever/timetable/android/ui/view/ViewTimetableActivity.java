package tomdrever.timetable.android.ui.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.TimetableActivityCodes;
import tomdrever.timetable.android.ui.edit.EditTimetableActivity;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityViewTimetableBinding;

public class ViewTimetableActivity extends AppCompatActivity {
    // TODO - return to this, find a better way. atm, it needs to be global to be reset.
    ActivityViewTimetableBinding binding;

    private TimetableContainer timetableContainer; // TODO - make observable
    private boolean hasTimetableChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get timetable
        Intent intent = getIntent();
        timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetabledetails");

        // Bind timetable to layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_timetable);
        binding.setTimetable(timetableContainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_timetable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(hasTimetableChanged ?
                            TimetableActivityCodes.VIEW_FINISHED_TIMETABLE_CHANGED
                            : TimetableActivityCodes.VIEW_FINISHED_TIMETABLE_UNCHANGED);
                    finish();
                }
            });
        }

        // If we know it is a new timetable, immediately launch edit timetable, for creation.
        if (intent.getBooleanExtra("isnewtimetable", false)){
            Intent launchEditIntent = new Intent(this, EditTimetableActivity.class);
            launchEditIntent.putExtra("timetabledetails", timetableContainer);
            launchEditIntent.putExtra("isnewtimetable", true);
            startActivityForResult(launchEditIntent, TimetableActivityCodes.EDIT_NEW_TIMETABLE_SUCCESSFUL_CODE);
        }

        FloatingActionButton editTimetableFAB = (FloatingActionButton) findViewById(R.id.edit_timetable_fab);

        if (editTimetableFAB != null) {
            editTimetableFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchEditIntent = new Intent(getBaseContext(), EditTimetableActivity.class);
                    launchEditIntent.putExtra("timetabledetails", timetableContainer);
                    startActivityForResult(launchEditIntent, TimetableActivityCodes.EDIT_NEW_TIMETABLE_SUCCESSFUL_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == TimetableActivityCodes.EDIT_NEW_TIMETABLE_SUCCESSFUL_CODE) {
                // Editing (or creation) has successfully finished
                // Unpack data and set to timetable object
                // FIXME - timetable must be rebound. Zut.
                timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetabledetails");
                binding.setTimetable(timetableContainer);
                hasTimetableChanged = true;
                Toast.makeText(this, "Saved changes", Toast.LENGTH_SHORT).show();
            } else if (resultCode == TimetableActivityCodes.EDIT_NEW_TIMETABLE_FAILED_CODE) {
                // Editing has finished, but changes have not been saved.

                // TODO - Do nothing?
                Toast.makeText(this, "Discarded changes", Toast.LENGTH_SHORT).show();
            } else if (resultCode == TimetableActivityCodes.CREATE_NEW_TIMETABLE_FAILED_CODE) {
                // Creation has finished, but changes have not been saved. Close
                // activity

                Toast.makeText(this, "Discarded timetable", Toast.LENGTH_SHORT).show();
                setResult(TimetableActivityCodes.VIEW_FINISHED_TIMETABLE_UNCHANGED);
                finish();
            }
        }
    }
}
