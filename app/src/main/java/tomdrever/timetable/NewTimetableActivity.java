package tomdrever.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import java.util.Calendar;

public class NewTimetableActivity extends AppCompatActivity{

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 200;
    protected static final int SUB_ACTIVITY_SUCCESS_CODE = 100;

    public TimetableDetails timetableDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timetable);
        Toolbar toolbar = (Toolbar)findViewById(R.id.setup_timetable_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_timetable, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_timetable:
                EditText etn = (EditText)findViewById(R.id.edit_timetable_name);
                EditText etd = (EditText)findViewById(R.id.edit_timetable_description);

                if (!etn.getText().toString().equals("")){
                    Intent i = new Intent(NewTimetableActivity.this, EditTimetableActivity.class);
                    startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
                    return true;
                }
                else{
                    Toast.makeText(this, "Name field is required!", Toast.LENGTH_SHORT).show();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED && requestCode == SUB_ACTIVITY_REQUEST_CODE){
            EditText etn = (EditText)findViewById(R.id.edit_timetable_name);
            EditText etd = (EditText)findViewById(R.id.edit_timetable_description);

            Timetable timetable = new Gson().fromJson(data.getBundleExtra("timetablebundle").getString("timetablejson"), Timetable.class);
            timetableDetails = new TimetableDetails(etn.getText().toString(), etd.getText().toString(), Calendar.getInstance().getTime(), timetable);

            Intent newData = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("timetabledetailsjson", new Gson().toJson(timetableDetails));
            newData.putExtra("timetabledetailsbundle", bundle);

            setResult(SUB_ACTIVITY_SUCCESS_CODE, newData);
            finish();
        }
    }
}
