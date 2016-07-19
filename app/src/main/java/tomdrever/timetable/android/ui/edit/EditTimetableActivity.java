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

import tomdrever.timetable.R;
import tomdrever.timetable.android.TimetableFileManager;
import tomdrever.timetable.android.ui.view.ViewTimetableActivity;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityEditTimetableBinding;

public class EditTimetableActivity extends AppCompatActivity {
    private TimetableContainer timetableContainer; // TODO - make observable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get timetable
        Intent intent = getIntent();
        timetableContainer = (TimetableContainer) intent.getSerializableExtra("timetabledetails");

        // Bind timetable to layout
        ActivityEditTimetableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_timetable);
        binding.setTimetable(timetableContainer);

        // Setup layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_timetable_toolbar);
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

        // Set up drag-sort-listview
        final RecyclerView daysListRecyclerView = (RecyclerView) findViewById(R.id.edit_timetable_days_list_recyclerview);

        final DaysRecyclerViewAdapter recyclerViewAdapter = new DaysRecyclerViewAdapter(timetableContainer.timetable.getDays(), this);

        assert daysListRecyclerView != null;
        daysListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        daysListRecyclerView.setAdapter(recyclerViewAdapter);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_day_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new day
                recyclerViewAdapter.add(new Day(String.format("Today %s", timetableContainer.timetable.getDays().size() + 1)));
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                return false;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(daysListRecyclerView);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
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
                // save over timetable
                new TimetableFileManager(this).save(timetableContainer);

                Intent intent = new Intent();
                intent.putExtra("timetabledetails", timetableContainer); // fileContents is, in this case, the data to be passed to viewtimetable as wel
                setResult(ViewTimetableActivity.EDIT_NEW_TIMETABLE_CODE, intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
