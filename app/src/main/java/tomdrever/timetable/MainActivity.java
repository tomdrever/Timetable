package tomdrever.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;

    ArrayList<TimeTableDetails> timetables;
    CustomRecyclerViewAdapter adapter;
    RecyclerView rv;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init base timetable details and handler
        timetables = new ArrayList<>();
        timetables.add(new TimeTableDetails("Timetable for tuesdays", "An interesting timetable", Calendar.getInstance().getTime(), new TimeTable()));
        timetables.add(new TimeTableDetails("My timetable", "", Calendar.getInstance().getTime(), new TimeTable()));

        // Set up recyclerview (the main timetable display list)
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);


        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new CustomRecyclerViewAdapter(timetables);
        rv.setAdapter(adapter);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewTimetableActivity.class);
                startActivityForResult(i,SUB_ACTIVITY_REQUEST_CODE);
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
        if(resultCode != RESULT_CANCELED && requestCode == SUB_ACTIVITY_REQUEST_CODE){
            timetables.add(new Gson().fromJson(data.getBundleExtra("timetabledetailsbundle").getString("timetabledetailsjson"), TimeTableDetails.class));

            // update cards, fade in new card

        }
    }
}
