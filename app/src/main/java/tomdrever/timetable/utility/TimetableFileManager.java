package tomdrever.timetable.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import tomdrever.timetable.data.Timetable;

public class TimetableFileManager {
    private String directory;
    private Context context;

    public TimetableFileManager(Context context) {
        this.context = context;
        directory = context.getFilesDir() + "timetables/";

        if (!new File(directory).exists()) {
            File fileDirectory = new File(directory);
            fileDirectory.mkdirs();
        }
    }

    public void save(Timetable timetable) {
        String fileContents = new Gson().toJson(timetable);

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

    public void delete(String name) {
        new File(directory + name).delete();
    }

    public Timetable load(String name) {
        try {
            return new Gson().fromJson(readFile(name), Timetable.class);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read file: " + name, Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Timetable> loadAll() {
        /*
        String[] fileNames = new File(directory).list();
        ArrayList<Timetable> initialTimetables = new ArrayList<>();

        for (String fileName : fileNames) {
            try {
                initialTimetables.add(new Gson().fromJson(readFile(fileName), Timetable.class));
            }
            catch (IOException e){
                Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }

        Timetable[] timetableArray = initialTimetables.toArray(new Timetable[0]);

        Arrays.sort(timetableArray);

        return timetableArray;
        */
        ArrayList<Timetable> timetables = new ArrayList<>();

        Timetable t1 = new Timetable();
        t1.setName("T1");
        t1.setDescription("Test desc 1");
        timetables.add(t1);

        Timetable t2 = new Timetable();
        t2.setName("T2");
        timetables.add(t2);

        Timetable t3 = new Timetable();
        t3.setName("T3");
        t3.setDescription("Test desc 3");
        timetables.add(t3);

        Timetable t4 = new Timetable();
        t4.setName("T4");
        timetables.add(t4);

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
