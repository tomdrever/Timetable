package tomdrever.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.Calendar;

public class EditTimetableActivity extends AppCompatActivity{

    protected static final int SUB_ACTIVITY_SUCCESS_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timetable);
        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_timetable_toolbar);
        setSupportActionBar(toolbar);
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
            case R.id.action_edit_timetable:
                Intent intent = getIntent();
                String name = intent.getStringExtra("timetablename");
                String description = intent.getStringExtra("timetabledescription");

                Timetable timetable = new Timetable();
                timetable.addDay(new Day("Monday"));
                timetable.addDay(new Day("Tuesday"));

                TimetableDetails timetableDetails = new TimetableDetails(name, description, Calendar.getInstance().getTime(), timetable);

                Intent data = new Intent();
                Bundle bundle = new Bundle();
                String timetableDetailsJson = new Gson().toJson(timetableDetails);
                bundle.putString("timetabledetailsjson", timetableDetailsJson);
                data.putExtra("timetabledetailsbundle", bundle);

                setResult(SUB_ACTIVITY_SUCCESS_CODE, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
