package tomdrever.timetable.android.ui.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import tomdrever.timetable.R;
import tomdrever.timetable.android.ui.TimetableActivityCodes;
import tomdrever.timetable.data.Timetable;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityTimetablesOverviewBinding;

public class TimetablesOverviewActivity extends AppCompatActivity {
    private TimetablesOverviewRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewAdapter = new TimetablesOverviewRecyclerViewAdapter(this);
        ActivityTimetablesOverviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_timetables_overview);
        binding.setTimetables(recyclerViewAdapter.timetables);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetables_overview_toolbar);
        setSupportActionBar(toolbar);

        // Set up recyclerview (the main timetable display list)
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.timetables_list_recyclerview);

        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Store timetable temporarily, so it can be restored
                final TimetableContainer tempTimetableDetails = recyclerViewAdapter.timetables.get(viewHolder.getAdapterPosition());
                final int tempPosition = viewHolder.getAdapterPosition();
                recyclerViewAdapter.remove(tempPosition);
                Snackbar.make(viewHolder.itemView, "Timetable deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restore
                        recyclerViewAdapter.add(tempTimetableDetails, tempPosition);
                    }
                }).show();
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_timetable_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ViewTimetableActivity.class);
                intent.putExtra("isnewtimetable", true);

                // Send new empty timetable with name and description
                intent.putExtra("timetabledetails", new TimetableContainer(
                        "",
                        "",
                        Calendar.getInstance().getTime(), new Timetable()));
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TimetableActivityCodes.VIEW_FINISHED_TIMETABLE_CHANGED) { // view timetable successful closed
            recyclerViewAdapter.loadTimetables();
        }
    }
}
