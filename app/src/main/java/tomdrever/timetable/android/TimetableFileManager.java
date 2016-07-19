package tomdrever.timetable.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import tomdrever.timetable.data.TimetableContainer;

public class TimetableFileManager {
    public String directory;
    private Context context;

    public TimetableFileManager(Context context) {
        // check if dir exists
        // File fileSaveLocation = new File(context.getFilesDir(), "timetables/");

        this.context = context;
        directory = context.getFilesDir() + "timetables/";

        if (!new File(directory).exists()) {
            File fileDirectory = new File(directory);
            fileDirectory.mkdirs();
        }
    }

    public void save(TimetableContainer timetableContainer) {
        // ttc -> json
        // save json

        String fileContents = new Gson().toJson(timetableContainer);

        try {
            File fileToSave = new File(directory, timetableContainer.name);
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not save file: " + timetableContainer.name, Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(String name) {
        new File(directory + name).delete();
    }

    public TimetableContainer load(String name) {
        // check if name exists in dir
        // load file
        // file = json -> timetable
        try {
            return new Gson().fromJson(readFile(name), TimetableContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read file: " + name, Toast.LENGTH_SHORT).show();

        }

        return null;
    }

    public ArrayList<TimetableContainer> loadAll() {
        // check dir, get all files
        // for each file, load to string, as json, to
        // timetable

        String[] fileNames = new File(directory).list();
        ArrayList<TimetableContainer> timetables = new ArrayList<>();

        for (String fileName : fileNames) {
            try {
                timetables.add(new Gson().fromJson(readFile(fileName), TimetableContainer.class));
            }
            catch (IOException e){
                Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }

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