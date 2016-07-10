package tomdrever.timetable.android.ui.edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import tomdrever.timetable.R;
import tomdrever.timetable.data.Day;
import tomdrever.timetable.data.TimetableContainer;
import tomdrever.timetable.databinding.ActivityEditDayBinding;
import tomdrever.timetable.databinding.ActivityViewTimetableBinding;

public class EditDayActivity extends AppCompatActivity {

    private Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get day from intent
        Intent intent = getIntent();
        day = new Gson().fromJson(intent.getStringExtra("dayjson"), Day.class);

        // Bind day to layout
        ActivityEditDayBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_day);
        binding.setDay(day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_day_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(100);
                finish();
            }
        });
    }
}
