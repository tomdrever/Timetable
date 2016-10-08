package tomdrever.timetable.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.gson.Gson;
import tomdrever.timetable.data.TimetableContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void save(TimetableContainer timetableContainer) {
        String fileContents = new Gson().toJson(timetableContainer);

        try {
            File fileToSave = new File(directory, timetableContainer.getName());
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not save file: " + timetableContainer.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(String name) {
        new File(directory + name).delete();
    }

    public TimetableContainer load(String name) {
        try {
            return new Gson().fromJson(readFile(name), TimetableContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read file: " + name, Toast.LENGTH_SHORT).show();

        }

        return null;
    }

    public ArrayList<TimetableContainer> loadAll() {
        String[] fileNames = new File(directory).list();
        ArrayList<TimetableContainer> initialTimetableContainers = new ArrayList<>();

        for (String fileName : fileNames) {
            try {
                initialTimetableContainers.add(new Gson().fromJson(readFile(fileName), TimetableContainer.class));
            }
            catch (IOException e){
                Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }

        TimetableContainer[] timetableContainersArray = initialTimetableContainers.toArray(new TimetableContainer[0]);

        Arrays.sort(timetableContainersArray);

        return new ArrayList<>(Arrays.asList(timetableContainersArray));
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
