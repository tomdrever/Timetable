package tomdrever.timetable.edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import tomdrever.timetable.R;
import tomdrever.timetable.databinding.ActivityEditTimetableBinding;
import tomdrever.timetable.structure.TimetableContainer;

public class EditTimetableActivity extends AppCompatActivity {

    private TimetableContainer timetableContainer; // TODO - make observable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        timetableContainer = new Gson().fromJson(intent.getStringExtra("timetabledetailsjson"), TimetableContainer.class);
        setTitle("Edit " + timetableContainer.name);

        ActivityEditTimetableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_timetable);
        binding.setTimetable(timetableContainer);

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
                Intent intent = new Intent();
                intent.putExtra("timetabledetailsjson", new Gson().toJson(timetableContainer));

                if (getIntent().getBooleanExtra("isnewtimetable", false)){ // is a new timetable
                    setResult(100, intent);
                }
                else { // not new, so editing
                    setResult(200, intent);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
