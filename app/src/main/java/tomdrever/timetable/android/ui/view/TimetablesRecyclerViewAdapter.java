package tomdrever.timetable.android.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import tomdrever.timetable.R;
import tomdrever.timetable.data.TimetableContainer;

public class TimetablesRecyclerViewAdapter extends RecyclerView.Adapter<TimetablesRecyclerViewAdapter.TimetableDetailViewHolder> {
    public final ArrayList<TimetableContainer> timetableDetails;
    private final Context context;

    public TimetablesRecyclerViewAdapter(ArrayList<TimetableContainer> timetableDetails, Context context){
        this.timetableDetails = timetableDetails;
        this.context = context;
    }

    @Override
    public TimetableDetailViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_card, parent, false);

        return new TimetableDetailViewHolder(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewTimetableActivity.class);
                intent.putExtra("timetabledetailsjson", new Gson().toJson(timetableDetails.get((int)v.getTag())));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, v.findViewById(R.id.timetable_card_base_view), "timetable_card");
                    ((Activity)context).startActivityForResult(intent, 100, options.toBundle());
                }
                else {
                    ((Activity)context).startActivityForResult(intent, 100);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(TimetableDetailViewHolder holder, int i) {
        // Format and add new details
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        holder.timetableDateCreatedView.setText("Created: " + df.format(timetableDetails.get(i).dateCreated));
        String desc = timetableDetails.get(i).description;
        holder.timetableDescriptionView.setText(desc != "" ? desc : "No description"); // android api stuffs. leave as !=
        holder.timetableNameView.setText(timetableDetails.get(i).name);

        holder.itemView.setTag(i);
    }

    public void add(TimetableContainer timetableContainer){
        add(timetableContainer, timetableDetails.size());
    }

    public void add(TimetableContainer timetableContainer, int position){
        // add to filesystem
        String fileName = timetableContainer.name;
        String fileContents = new Gson().toJson(timetableContainer);

        // (try to) save
        try {
            File fileToSave = new File(context.getFilesDir(), "timetables/" + fileName);
            FileOutputStream outputStream = new FileOutputStream(fileToSave);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Could not save file: " + fileName, Toast.LENGTH_SHORT).show();
        }

        // add to recyclerview
        timetableDetails.add(position, timetableContainer);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, timetableDetails.size());
    }

    public void remove(int position) {
        // delete from filesysytem
        File fileToDelete = new File(context.getFilesDir() + "/timetables/" + timetableDetails.get(position).name);
        fileToDelete.delete();

        // delete from recyclerview
        timetableDetails.remove(timetableDetails.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, timetableDetails.size());
    }

    public void loadTimetables() {
        // Clear
        clearTimetables();

        // Get files location
        File fileSaveLocation = new File(context.getFilesDir(), "timetables/");
        String[] fileNames = fileSaveLocation.list();

        // Check the files exist
        if (fileNames != null) {
            for (String fileName : fileNames) {
                try {
                    add(new Gson().fromJson(readFile(fileName), TimetableContainer.class));
                }
                catch (IOException e){
                    Toast.makeText(context, "Error: Could not read file: " + fileName, Toast.LENGTH_SHORT).show();
                }
            }

            notifyDataSetChanged();
        }
    }

    public void clearTimetables() {
        timetableDetails.clear();
        notifyDataSetChanged();
    }

    @NonNull
    private String readFile(String file) throws IOException {
        File fileToRead = new File(context.getFilesDir(), "timetables/" + file);
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return timetableDetails.size();
    }

    public static class TimetableDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView timetableNameView;
        private TextView timetableDateCreatedView;
        private TextView timetableDescriptionView;
        private View itemView;

        public TimetableDetailViewHolder(final View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(onClickListener);

            timetableNameView = (TextView) itemView.findViewById(R.id.timetable_name);
            timetableDateCreatedView = (TextView) itemView.findViewById(R.id.timetable_date_created);
            timetableDescriptionView = (TextView) itemView.findViewById(R.id.timetable_description);
        }
    }
}