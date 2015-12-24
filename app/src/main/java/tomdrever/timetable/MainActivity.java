package tomdrever.timetable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;

    private ArrayList<TimetableDetails> timetables;
    private CustomRecyclerViewAdapter adapter;
    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init base timetable details and handler
        timetables = new ArrayList<>();

        // Load timetables from filedir
        String[] files = getFilesDir().list();

        for (String file : files) {
            try {
                timetables.add(new Gson().fromJson(readFile(file), TimetableDetails.class));
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

        adapter = new CustomRecyclerViewAdapter(timetables, this);
        rv.setAdapter(adapter);

        // Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewTimetableActivity.class);
                startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
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

        return fileContent.toString();
    }

    private void saveFile(String fileName, String fileContents) throws IOException {
        FileOutputStream outputStream;
        outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && requestCode == SUB_ACTIVITY_REQUEST_CODE) {
            updateTimetables(data.getExtras().getBundle("timetabledetailsbundle").getString("timetabledetailsjson"));
        }
    }

    private void updateTimetables(String timetablejson){
        // Add new timetable json to filedir
        TimetableDetails timetableDetails = new Gson().fromJson(timetablejson, TimetableDetails.class);

        try{
            saveFile(timetableDetails.name, timetablejson);
        }
        catch (IOException e){
            alert("Error", "Error saving file: " + timetableDetails.name);
        }

        // Add new card and update
        timetables.add(timetableDetails);
        adapter.notifyItemInserted(timetables.size() + 1);
        rv.smoothScrollToPosition(timetables.size());
    }
}
