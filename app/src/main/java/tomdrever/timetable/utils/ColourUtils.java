package tomdrever.timetable.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ColourUtils {
    private static Gson gson;

    public static int lighten(int color) {
        return Color.argb(Color.alpha(color) - 110, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int green = Color.rgb(67,160,71);

    public static void addCustomColour(String name, int colour, Context context) {
        // load list, add, save
        HashMap<String, Integer> colours = (HashMap<String, Integer>) loadCustomColours(context);
        colours.put(name, colour);
        saveCustomColours(colours, context);
    }

    public static void removeCustomColour(String name, int colour, Context context) {
        // load list, remove, save
        HashMap<String, Integer> colours = (HashMap<String, Integer>) loadCustomColours(context);
        colours.remove(name);
        saveCustomColours(colours, context);
    }

    public static void saveCustomColours(Map<String, Integer> colours, Context context) {
        if (gson == null) gson = Converters.registerLocalTime(new GsonBuilder()).create();

        FileUtils.writeToFile(context.getFilesDir().getAbsolutePath(), "colours.txt", gson.toJson(colours));
    }

    public static HashMap<String, Integer> loadCustomColours(Context context) {
        if (gson == null) gson = Converters.registerLocalTime(new GsonBuilder()).create();

        HashMap<String, Integer> loadedColours = null;

        try {
            Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
            loadedColours =  gson.fromJson(FileUtils.readFile(context.getFilesDir().getAbsolutePath(), "colours.txt"), type);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not read colours file", Toast.LENGTH_SHORT).show();
        }

        return loadedColours == null ? new HashMap<String, Integer>() : loadedColours;
    }
}
