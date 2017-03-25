package tomdrever.timetable.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    @NonNull
    public static String readFile(String directory, String file) throws IOException {
        File fileToRead = new File(directory + "/" +file);
        fileToRead.createNewFile();
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

    public static void writeToFile(String directory, String file, String contents) {
        try {
            File fileToSave = new File(directory + "/" + file);
            fileToSave.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(contents.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
