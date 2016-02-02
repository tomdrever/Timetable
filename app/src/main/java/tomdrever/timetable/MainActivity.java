package tomdrever.timetable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tomdrever.timetable.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainRecyclerViewAdapter adapter;
    private RecyclerView rv;
    private LinearLayoutManager llm;

    private ObservableArrayList<TimetableContainer> timetables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timetables = new ObservableArrayList<>();
        adapter = new MainRecyclerViewAdapter(timetables, this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setTimetables(timetables);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load timetableDetailsList from filedir
        String[] files = getFilesDir().list();

        for (String file : files) {
            try {
                timetables.add(new Gson().fromJson(readFile(file), TimetableContainer.class));
            }
            catch (IOException e){
                alert("Error", "Could not read file: " + file);
            }
        }

        // Set up recyclerview (the main timetable display list)
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTimetableDialogFragment newTimetableDialogFragment = new NewTimetableDialogFragment();
                newTimetableDialogFragment.show(getFragmentManager(), "NewTimetableTag");
            }
        });
    }

    @NonNull
    private String readFile(String file) throws IOException {
        FileInputStream inputStream = openFileInput(file);
        StringBuilder fileContent = new StringBuilder("");
        byte[] buffer = new byte[1024];

        int n;
        while ((n = inputStream.read(buffer)) != -1)
        {
            fileContent.append(new String(buffer, 0, n));
        }
        inputStream.close();
        return fileContent.toString();
    }

    private void saveFile(String fileName, String fileContents) throws IOException {
        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(fileContents.getBytes());
        outputStream.close();
    }

    private void alert (String alertTitle, String alertMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertMessage)
                .setTitle(alertTitle);
        AlertDialog dialog = builder.create();
        dialog.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 100){ // new
                newTimetablesEntry(intent.getStringExtra("timetabledetailsjson"));
            }
            else if (requestCode == 200){ // edit
                updateTimetablesEntry(intent.getStringExtra("timetabledetailsjson"));
            }
        }
    }

    private void newTimetablesEntry(String timetablejson){
        // Add new timetable json to filedir
        TimetableContainer timetableContainer = new Gson().fromJson(timetablejson, TimetableContainer.class);

        try{
            saveFile(timetableContainer.name, timetablejson);
        }
        catch (IOException e){
            alert("Error", "Error saving file: " + timetableContainer.name);
        }

        // Add new card and update
        adapter.add(timetableContainer);
        rv.smoothScrollToPosition(timetables.size() + 1);
    }

    private void updateTimetablesEntry(String timetablejson){
        // Add new timetable json to filedir
        TimetableContainer newTimetableContainer = new Gson().fromJson(timetablejson, TimetableContainer.class);
        for(TimetableContainer timetableContainer : timetables){ // UURGGH
            if (timetableContainer.name.equals(newTimetableContainer.name)) {
                timetables.set(timetables.indexOf(timetableContainer), newTimetableContainer);
            }
        }

        try{
            saveFile(newTimetableContainer.name, timetablejson);
        }
        catch (IOException e){
            alert("Error", "Error saving file: " + newTimetableContainer.name);
        }
    }
}
