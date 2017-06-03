package tomdrever.timetable.utils;

import android.content.Context;
import android.widget.Toast;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import tomdrever.timetable.data.Timetable;

public class TimetableFileManager {
    private String directory;
    private Context context;

    private static Gson gson;

    public TimetableFileManager(Context context) {
        this.context = context;
        directory = context.getFilesDir()+ "/" + "timetables/";

        if (!new File(directory).exists()) {
            File fileDirectory = new File(directory);
            fileDirectory.mkdirs();
        }

        gson = Converters.registerLocalTime(new GsonBuilder()).create();
    }

    public void save(Timetable timetable) {
        FileUtils.writeToFile(directory, timetable.getId() + ".txt",
                gson.toJson(timetable));
    }

    public void saveAll(ArrayList<Timetable> timetables) {
        for (Timetable timetable : timetables) {
            save(timetable);
        }
    }

    public void delete(String id) {
        new File(directory + id + ".txt").delete();
    }

    public Timetable load(String fileName) {
        try {
            return gson.fromJson(FileUtils.readFile(directory, fileName), Timetable.class);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Timetable> loadAll() {

        String[] fileNames = new File(directory).list();
        ArrayList<Timetable> timetables = new ArrayList<>();

        for (String fileName : fileNames) {
            timetables.add(load(fileName));
        }

        // Sort by index
        Collections.sort(timetables);

        return timetables;
    }
}
