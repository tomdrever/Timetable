package tomdrever.timetable.android.ui.edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.Strings;

import java.util.Collections;

import tomdrever.timetable.R;
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.android.ui.TimetableActivityCodes;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityEditTimetableBinding;

public class EditTimetableActivity extends AppCompatActivity {
    private TimetableContainer timetableContainer; // TODO - make observable
    private boolean isNewTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get timetable
        Intent intent = getIntent();
        isNewTimetable = intent.getBooleanExtra("isnewtimetable", false);
        timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetabledetails");

        // Bind timetable to layout
        ActivityEditTimetableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_timetable);
        binding.setTimetable(timetableContainer);
        binding.setIsnewtimetable(isNewTimetable);

        // Setup layout w/ back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_timetable_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        // Set up drag-sort-listview
        final RecyclerView daysListRecyclerView = (RecyclerView) findViewById(R.id.edit_timetable_days_list_recyclerview);

        final DaysRecyclerViewAdapter recyclerViewAdapter = new DaysRecyclerViewAdapter(timetableContainer.getTimetable().getDays(), this);

        daysListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        daysListRecyclerView.setAdapter(recyclerViewAdapter);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_day_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new day
                recyclerViewAdapter.add(new Day(String.format("Today %s", timetableContainer.getTimetable().getDays().size() + 1)));
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean isLongPressDragEnabled() {
                return super.isLongPressDragEnabled();
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Day tempDay = recyclerViewAdapter.days.get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                recyclerViewAdapter.remove(tempPosition);
                Snackbar.make(viewHolder.itemView, "Day deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Re-add
                        recyclerViewAdapter.add(tempDay, tempPosition);
                    }
                }).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(recyclerViewAdapter.days, viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(daysListRecyclerView);

        // Set name and description to text boxes
        if (!isNewTimetable) {
            ((EditText)findViewById(R.id.edit_timetable_name)).setText(timetableContainer.getName());
            ((EditText)findViewById(R.id.edit_timetable_description))
                    .setText(timetableContainer.getDescription());
        }
    }

    @Override
    public void onBackPressed() {
        // TODO - "Discard saved changes and quit?"
        setResult(isNewTimetable ?
                TimetableActivityCodes.CREATE_NEW_TIMETABLE_FAILED_CODE
                : TimetableActivityCodes.EDIT_NEW_TIMETABLE_FAILED_CODE);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish_editing_timetable: // On "done" pressed
                EditText editTimetableName = (EditText) findViewById(R.id.edit_timetable_name);
                // Check the timetable has been given a name
                if (Strings.isNullOrEmpty(editTimetableName.getText().toString().trim())) {
                    // If not, tell the user
                    Toast.makeText(this, "The timetable needs a name!", Toast.LENGTH_SHORT).show();

                    // TODO - highlight name field to the user in some way
                } else {
                    // TODO - if (isnewtimetable) - check if the name is already used.
                    // If so, ask the user if they want to overwrite the tt or go back

                    TimetableFileManager fileManager = new TimetableFileManager(this);

                    // If it's not a new timetable, delete the old file (jic)
                    if (!isNewTimetable) {
                        fileManager.delete(timetableContainer.getName());
                    }

                    // Set name and description from text
                    timetableContainer.setName(editTimetableName.getText().toString());

                    EditText editTimetableDescription = (EditText)findViewById(R.id.edit_timetable_description);
                    if (Strings.isNullOrEmpty(editTimetableDescription.getText().toString().trim())) {
                        timetableContainer.setDescription("No description");
                    } else {
                        timetableContainer.setDescription(editTimetableDescription.getText().toString());
                    }

                    // Save over timetable
                    fileManager.save(timetableContainer);

                    Intent intent = new Intent();
                    intent.putExtra("timetabledetails", timetableContainer);
                    setResult(TimetableActivityCodes.EDIT_NEW_TIMETABLE_SUCCESSFUL_CODE, intent);
                    finish();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
