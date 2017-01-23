package tomdrever.timetable.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        directory = context.getFilesDir() + "timetables/";

        if (!new File(directory).exists()) {
            File fileDirectory = new File(directory);
            fileDirectory.mkdirs();
        }

        gson = Converters.registerLocalTime(new GsonBuilder()).create();
    }

    public void save(Timetable timetable) {
        String fileContents = gson.toJson(timetable);

        try {
            File fileToSave = new File(directory, timetable.getName());
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not save file: " + timetable.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveAll(ArrayList<Timetable> timetables) {
        for (Timetable timetable : timetables) {
            save(timetable);
        }
    }

    public void delete(String name) {
        new File(directory + name).delete();
    }

    public Timetable load(String name) {
        try {
            return gson.fromJson(readFile(name), Timetable.class);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read file: " + name, Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Timetable> loadAll() {

        String[] fileNames = new File(directory).list();
        ArrayList<Timetable> timetables = new ArrayList<>();

        for (String fileName : fileNames) {
            try {
                timetables.add(gson.fromJson(readFile(fileName), Timetable.class));
            }
            catch (IOException e){
                Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }

        // Sort by index
        Collections.sort(timetables);

        return timetables;
    }

    @NonNull
    private String readFile(String file) throws IOException {
        File fileToRead = new File(directory, file);
        FileInputStream inputStream = new FileInputStream(fileToRead);
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
}
